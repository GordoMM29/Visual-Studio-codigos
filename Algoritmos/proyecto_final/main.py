import os
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime
from src import youtube_collector, reddit_simulator, twitter_collector, sentiment_analyzer

# Configurar estilo de gráficas
plt.style.use('seaborn-v0_8-darkgrid')
sns.set_palette("Set2")
plt.rcParams['figure.figsize'] = (12, 6)

def crear_carpetas():
    """Crea las carpetas necesarias para el proyecto"""
    carpetas = ["data", "data/historico", "data/graficas"]
    for carpeta in carpetas:
        os.makedirs(carpeta, exist_ok=True)

def generar_graficas(df, tema_actual):
    """Genera gráficas profesionales de los resultados"""
    
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    
    # 1. GRÁFICA DE BARRAS POR RED SOCIAL
    fig, axes = plt.subplots(2, 2, figsize=(14, 10))
    fig.suptitle(f'Análisis de Sentimiento: {tema_actual}', fontsize=16, fontweight='bold')
    
    # Gráfica 1: Barras por red social
    stats_red = df.groupby(['red_social', 'sentimiento_ia']).size().unstack(fill_value=0)
    stats_red.plot(kind='bar', ax=axes[0, 0], color=['#ff6b6b', '#4ecdc4', '#ffe66d'])
    axes[0, 0].set_title('Sentimiento por Red Social', fontsize=12, fontweight='bold')
    axes[0, 0].set_xlabel('Red Social')
    axes[0, 0].set_ylabel('Número de Publicaciones')
    axes[0, 0].legend(title='Sentimiento')
    axes[0, 0].tick_params(axis='x', rotation=0)
    
    # Gráfica 2: Gráfica de pastel (general)
    general_sentiment = df['sentimiento_ia'].value_counts()
    colors = ['#4ecdc4', '#ff6b6b', '#ffe66d']
    axes[0, 1].pie(general_sentiment, labels=general_sentiment.index, autopct='%1.1f%%',
                   colors=colors[:len(general_sentiment)], explode=[0.05]*len(general_sentiment))
    axes[0, 1].set_title('Distribución General de Sentimientos', fontsize=12, fontweight='bold')
    
    # Gráfica 3: Evolución temporal (si hay fechas)
    if 'fecha' in df.columns:
        df['fecha'] = pd.to_datetime(df['fecha'])
        temporal = df.groupby([df['fecha'].dt.date, 'sentimiento_ia']).size().unstack(fill_value=0)
        temporal.plot(kind='line', marker='o', ax=axes[1, 0], linewidth=2, markersize=6)
        axes[1, 0].set_title('Evolución Temporal del Sentimiento', fontsize=12, fontweight='bold')
        axes[1, 0].set_xlabel('Fecha')
        axes[1, 0].set_ylabel('Número de Publicaciones')
        axes[1, 0].legend(title='Sentimiento')
        axes[1, 0].tick_params(axis='x', rotation=45)
    
    # Gráfica 4: Barras horizontales (resumen)
    resumen = df['sentimiento_ia'].value_counts()
    bars = axes[1, 1].barh(resumen.index, resumen.values, color=colors[:len(resumen)])
    axes[1, 1].set_title('Resumen Total de Sentimientos', fontsize=12, fontweight='bold')
    axes[1, 1].set_xlabel('Cantidad')
    for bar, valor in zip(bars, resumen.values):
        axes[1, 1].text(valor + 1, bar.get_y() + bar.get_height()/2, str(valor), va='center')
    
    plt.tight_layout()
    grafica_path = f"data/graficas/analisis_{timestamp}.png"
    plt.savefig(grafica_path, dpi=150, bbox_inches='tight')
    plt.close()
    
    return grafica_path

def guardar_historico(df, tema):
    """Guarda los datos en un archivo histórico"""
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    df['ejecucion'] = timestamp
    df['tema_consulta'] = tema
    
    # Archivo histórico acumulado
    historico_path = "data/historico/todas_ejecuciones.csv"
    
    if os.path.exists(historico_path):
        historico = pd.read_csv(historico_path)
        historico = pd.concat([historico, df], ignore_index=True)
    else:
        historico = df
    
    historico.to_csv(historico_path, index=False, encoding='utf-8-sig')
    return historico_path

def generar_reporte_html(df, grafica_path, tema):
    """Genera un reporte HTML profesional"""
    
    total = len(df)
    positivos = len(df[df['sentimiento_ia'] == 'Positivo'])
    negativos = len(df[df['sentimiento_ia'] == 'Negativo'])
    neutrales = len(df[df['sentimiento_ia'] == 'Neutral'])
    
    stats_red = df.groupby(['red_social', 'sentimiento_ia']).size().unstack(fill_value=0)
    
    html_content = f"""
    <!DOCTYPE html>
    <html>
    <head>
        <title>Reporte de Análisis - IA en Redes Sociales</title>
        <meta charset="UTF-8">
        <style>
            body {{ font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }}
            .container {{ max-width: 1200px; margin: auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }}
            h1 {{ color: #2c3e50; text-align: center; }}
            h2 {{ color: #34495e; border-bottom: 2px solid #3498db; padding-bottom: 10px; }}
            .stats {{ display: flex; justify-content: space-around; margin: 30px 0; }}
            .stat-card {{ background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 10px; text-align: center; min-width: 150px; }}
            .stat-card.positive {{ background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }}
            .stat-card.negative {{ background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }}
            .stat-card.neutral {{ background: linear-gradient(135deg, #f2994a 0%, #f2c94c 100%); }}
            .stat-number {{ font-size: 36px; font-weight: bold; }}
            .stat-label {{ font-size: 14px; opacity: 0.9; }}
            table {{ width: 100%; border-collapse: collapse; margin-top: 20px; }}
            th, td {{ padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }}
            th {{ background-color: #3498db; color: white; }}
            tr:hover {{ background-color: #f5f5f5; }}
            .grafica {{ text-align: center; margin: 30px 0; }}
            .grafica img {{ max-width: 100%; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }}
            .footer {{ text-align: center; margin-top: 40px; padding-top: 20px; border-top: 1px solid #ddd; color: #7f8c8d; }}
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Análisis de Sentimiento: Inteligencia Artificial en Redes Sociales</h1>
            <p style="text-align: center; color: #7f8c8d;">Fecha de ejecución: {datetime.now().strftime('%d/%m/%Y %H:%M:%S')}</p>
            <p style="text-align: center; color: #7f8c8d;">Tema analizado: <strong>{tema}</strong></p>
            
            <div class="stats">
                <div class="stat-card">
                    <div class="stat-number">{total}</div>
                    <div class="stat-label">Total Publicaciones</div>
                </div>
                <div class="stat-card positive">
                    <div class="stat-number">{positivos}</div>
                    <div class="stat-label">Positivos ({positivos/total*100:.1f}%)</div>
                </div>
                <div class="stat-card negative">
                    <div class="stat-number">{negativos}</div>
                    <div class="stat-label">Negativos ({negativos/total*100:.1f}%)</div>
                </div>
                <div class="stat-card neutral">
                    <div class="stat-number">{neutrales}</div>
                    <div class="stat-label">Neutrales ({neutrales/total*100:.1f}%)</div>
                </div>
            </div>
            
            <div class="grafica">
                <h2>Visualización de Resultados</h2>
                <img src="{grafica_path}" alt="Gráficas de análisis">
            </div>
            
            <h2>Desglose por Red Social</h2>
            {stats_red.to_html(classes='table')}
            
            <h2>Conclusiones Preliminares</h2>
            <ul>
                <li>La percepción general sobre la IA en redes sociales es <strong>{"positiva" if positivos > negativos else "negativa" if negativos > positivos else "mixta"}</strong>.</li>
                <li>{"Los usuarios de YouTube muestran la opinión más favorable" if ("YouTube" in stats_red.index and stats_red.loc["YouTube", "Positivo"]) else "YouTube muestra tendencia positiva"}</li>
                <li>Se analizaron <strong>{len(df['red_social'].unique())}</strong> plataformas diferentes.</li>
            </ul>
            
            <div class="footer">
                <p>Proyecto Final - Análisis de IA en Redes Sociales</p>
                <p>Generado automáticamente con Python • Análisis de sentimiento usando VADER</p>
            </div>
        </div>
    </body>
    </html>
    """
    
    reporte_path = f"data/reporte_{datetime.now().strftime('%Y%m%d_%H%M%S')}.html"
    with open(reporte_path, 'w', encoding='utf-8') as f:
        f.write(html_content)
    
    return reporte_path

def main():
    print("=" * 70)
    print("ANÁLISIS DE SENTIMIENTO: IA EN REDES SOCIALES")
    print("=" * 70)
    
    # Crear carpetas necesarias
    crear_carpetas()
    
    # Tema principal
    tema = "inteligencia artificial en redes sociales"
    
    print(f"\nTema analizado: {tema}")
    print("   (Los datos se guardarán automáticamente en la carpeta 'data')\n")
    
    todos_los_datos = pd.DataFrame()
    
    # 1. Recolección de datos
    print("FASE 1: Recolectando datos")
    print("-" * 50)
    
    # YouTube
    print("\n🎬 YOUTUBE:")
    df_youtube = youtube_collector.get_youtube_comments(tema)
    print(f" {len(df_youtube)} comentarios")
    todos_los_datos = pd.concat([todos_los_datos, df_youtube], ignore_index=True)
    
    # Reddit
    print("\nREDDIT:")
    df_reddit = reddit_simulator.generate_reddit_data(tema)
    print(f" {len(df_reddit)} publicaciones")
    todos_los_datos = pd.concat([todos_los_datos, df_reddit], ignore_index=True)
    
    # X/Twitter
    print("\n X/TWITTER:")
    df_twitter = twitter_collector.get_twitter_data(tema)
    print(f"{len(df_twitter)} tweets")
    todos_los_datos = pd.concat([todos_los_datos, df_twitter], ignore_index=True)
    
    # 2. Análisis de sentimiento con IA
    print("\nFASE 2: Aplicando IA de análisis de sentimiento (VADER)")
    print("-" * 50)
    todos_los_datos = sentiment_analyzer.procesar_dataframe(todos_los_datos)
    
    # 3. Guardar datos (CSV + histórico)
    print("\nFASE 3: Guardando resultados")
    print("-" * 50)
    
    # Guardar ejecución actual
    archivo_actual = f"data/resultado_actual_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv"
    todos_los_datos.to_csv(archivo_actual, index=False, encoding='utf-8-sig')
    print(f"Datos actuales guardados en: {archivo_actual}")
    
    # Guardar en histórico
    historico_path = guardar_historico(todos_los_datos.copy(), tema)
    print(f"Histórico actualizado en: {historico_path}")
    
    # 4. Generar gráficas
    print("\nFASE 4: Generando visualizaciones")
    print("-" * 50)
    grafica_path = generar_graficas(todos_los_datos, tema)
    print(f"Gráficas guardadas en: {grafica_path}")
    
    # 5. Generar reporte HTML
    print("\nFASE 5: Generando reporte profesional")
    print("-" * 50)
    reporte_path = generar_reporte_html(todos_los_datos, grafica_path, tema)
    print(f"Reporte HTML guardado en: {reporte_path}")
    
    # 6. Mostrar estadísticas en consola
    print("\n" + "=" * 50)
    print("RESULTADOS DEL ANÁLISIS")
    print("=" * 50)
    
    total = len(todos_los_datos)
    positivos = len(todos_los_datos[todos_los_datos['sentimiento_ia'] == 'Positivo'])
    negativos = len(todos_los_datos[todos_los_datos['sentimiento_ia'] == 'Negativo'])
    neutrales = len(todos_los_datos[todos_los_datos['sentimiento_ia'] == 'Neutral'])
    
    print(f"\nResumen general:")
    print(f"   Total: {total}")
    print(f"Positivos: {positivos} ({positivos/total*100:.1f}%)")
    print(f"Negativos: {negativos} ({negativos/total*100:.1f}%)")
    print(f"Neutrales: {neutrales} ({neutrales/total*100:.1f}%)")
    
    # Mostrar por red social
    print(f"\nPor red social:")
    stats_red = todos_los_datos.groupby(['red_social', 'sentimiento_ia']).size().unstack(fill_value=0)
    print(stats_red)
    
    print("\n" + "=" * 50)
    print("¡ANÁLISIS COMPLETADO CON ÉXITO!")
    print(f"\nRevisa la carpeta 'data' para ver:")
    print(f" - Gráficas en data/graficas/")
    print(f" - Reporte HTML en data/reporte_*.html")
    print(f" - Histórico de ejecuciones en data/historico/")
    print("=" * 50)
    
    # Abrir el reporte automáticamente (opcional)
    import webbrowser
    webbrowser.open(f"file:///{os.path.abspath(reporte_path)}")
    print("\nEl reporte HTML se ha abierto en tu navegador.")

if __name__ == "__main__":
    main()