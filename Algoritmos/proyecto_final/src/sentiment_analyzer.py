import pandas as pd
import nltk
from nltk.sentiment.vader import SentimentIntensityAnalyzer

# Descargar el analizador de VADER (solo la primera vez)
nltk.download('vader_lexicon', quiet=True)

def analizar_sentimiento(texto):
    """Usa IA (VADER) para clasificar un texto como Positivo o Negativo."""
    analyzer = SentimentIntensityAnalyzer()
    puntuacion = analyzer.polarity_scores(texto)
    
    # compound va de -1 (muy negativo) a +1 (muy positivo)
    if puntuacion['compound'] >= 0.05:
        return 'Positivo'
    elif puntuacion['compound'] <= -0.05:
        return 'Negativo'
    else:
        return 'Neutral'

def procesar_dataframe(df):
    """Aplica análisis de sentimiento a todo el dataframe."""
    if df.empty:
        return df
    
    df['sentimiento_ia'] = df['texto'].apply(analizar_sentimiento)
    return df