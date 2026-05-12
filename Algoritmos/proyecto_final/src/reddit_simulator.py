import pandas as pd
import random
from datetime import datetime, timedelta

def generate_reddit_data(keyword, days=7, posts_por_dia=15):
    """Genera datos simulados de Reddit para el proyecto."""
    data = []
    
    # Frases de ejemplo (puedes personalizarlas)
    frases_positivas = [
        "¡Excelente post! Me encantó el análisis, muy completo ",
        "Gran comunidad, siempre aprendo algo nuevo aquí",
        "Qué bien explicado, gracias por compartir",
        "Esto es justo lo que necesitaba, muy útil",
        "Increíble el nivel de detalle, 10/10",
        "Me encanta cómo abordaron este tema",
        "Muy recomendable, se nota el trabajo detrás"
    ]
    
    frases_negativas = [
        "No me gustó para nada, bastante pobre el contenido",
        "Desinformación total, deberían verificar fuentes",
        "Qué pérdida de tiempo, esperaba más",
        "Totalmente en desacuerdo, argumentos muy débiles",
        "Bait o mala fe? No aporta nada útil",
        "Otra vez con lo mismo, cansan estos posts",
        "Calidad pésima, no vale la pena leerlo"
    ]
    
    frases_neutrales = [
        "Interesante punto, habría que ver más fuentes",
        "No estoy seguro, necesito investigar más",
        "Dato curioso, aunque falta contexto",
        "Vamos a ver qué opinan los demás",
        "Habría que profundizar en este aspecto"
    ]
    
    for day in range(days):
        fecha = (datetime.now() - timedelta(days=day)).strftime("%Y-%m-%d")
        
        for i in range(posts_por_dia):
            # Distribución: 50% positivo, 35% negativo, 15% neutral
            rand = random.random()
            if rand < 0.5:
                texto = random.choice(frases_positivas)
                sentimiento = "Positivo"
            elif rand < 0.85:
                texto = random.choice(frases_negativas)
                sentimiento = "Negativo"
            else:
                texto = random.choice(frases_neutrales)
                sentimiento = "Neutral"
            
            data.append({
                'fecha': fecha,
                'red_social': 'Reddit',
                'usuario': f'u_{random.randint(1000, 9999)}',
                'tema': keyword,
                'texto': texto,
                'sentimiento': sentimiento  # Pre-clasificado para referencia
            })
    
    return pd.DataFrame(data)