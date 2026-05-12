import os
import pandas as pd
from src import youtube_collector, reddit_simulator, sentiment_analyzer

def main():
    print("=" * 60)
    print("ANÁLISIS DE SENTIMIENTO EN REDES SOCIALES CON IA")
    print("=" * 60)
    
    temas = ["inteligencia artificial Facebook","IA Instagram algoritmo","machine learning TikTok"]
    
    os.makedirs("data", exist_ok=True)
    
    todos_los_datos = pd.DataFrame()
    
    print("\nRecolectando datos...")
    
    for tema in temas:
        print(f"\n▶Tema: {tema}")
        
        # YouTube (simulado)
        print("  🎬 Generando datos de YouTube...")
        df_youtube = youtube_collector.get_youtube_comments(tema)
        print(f"      {len(df_youtube)} comentarios generados")
        todos_los_datos = pd.concat([todos_los_datos, df_youtube], ignore_index=True)
        
        # Reddit (simulado)
        print("   Generando datos de Reddit...")
        df_reddit = reddit_simulator.generate_reddit_data(tema)
        print(f"      {len(df_reddit)} publicaciones generadas")
        todos_los_datos = pd.concat([todos_los_datos, df_reddit], ignore_index=True)
    
    print("\n Aplicando IA para análisis de sentimiento...")
    todos_los_datos = sentiment_analyzer.procesar_dataframe(todos_los_datos)
    
    archivo_salida = "data/resultado_analisis.csv"
    todos_los_datos.to_csv(archivo_salida, index=False, encoding='utf-8-sig')
    print(f"\n Resultados guardados en: {archivo_salida}")
    
    print("\n ESTADÍSTICAS FINALES")
    print("-" * 40)
    
    stats = todos_los_datos.groupby(['red_social', 'sentimiento_ia']).size().unstack(fill_value=0)
    print("\nPor red social:")
    print(stats)
    
    total = len(todos_los_datos)
    positivos = len(todos_los_datos[todos_los_datos['sentimiento_ia'] == 'Positivo'])
    negativos = len(todos_los_datos[todos_los_datos['sentimiento_ia'] == 'Negativo'])
    
    print(f"\n Resumen general:")
    print(f"  Total: {total}")
    print(f"   Positivos: {positivos} ({positivos/total*100:.1f}%)")
    print(f"   Negativos: {negativos} ({negativos/total*100:.1f}%)")
    
    print("\n ¡Análisis completado!")

if __name__ == "__main__":
    main()