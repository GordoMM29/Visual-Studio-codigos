# src/twitter_collector.py
import pandas as pd
import random
from datetime import datetime, timedelta

MI_AUTH_TOKEN = "fe0c82afb85c50c95bac4e3c8dfc2b685ad7b701"
MI_CT0 = "7f64efdadfd7b4531eae0d1e6b9e2cac1323dc8bb0b62ac29428abb2a8c6fe9de2d449ba66548eaaf3a4940f73b4837ec9a045af542d464e514066441ea665487f358d9ff8bc0bf093b399c08be9f34a"

# Intentar importar birdapi
try:
    from bird import TwitterClient
    BIRD_AVAILABLE = True
except ImportError:
    BIRD_AVAILABLE = False
    print("birdapi no instalado. Ejecuta: pip install birdapi")

# Frases simuladas para fallback
TWEETS_POSITIVOS = [
    "La IA en X/Twitter está revolucionando cómo consumo noticias, todo es más relevante ahora 🚀",
    "El algoritmo de X es impresionante, siempre encuentra contenido que me interesa",
    "Me encanta cómo la IA filtra el spam y el contenido dañino en X",
    "Las recomendaciones con IA de Twitter son muy precisas, gran trabajo",
    "La inteligencia artificial detrás de X mejora mi experiencia cada día"
]

TWEETS_NEGATIVOS = [
    "El algoritmo de X solo muestra contenido polarizante, ya no es lo mismo",
    "Desde que cambiaron la IA en Twitter, mi timeline está lleno de cosas que no me interesan",
    "La moderación automática con IA de X es pésima, banea cuentas sin razón",
    "No entiendo cómo funciona el algoritmo de X, mis tweets no llegan a nadie",
    "X usa IA para manipular lo que vemos, perdieron mi confianza"
]

TWEETS_NEUTRALES = [
    "Interesante cómo la IA está transformando X/Twitter",
    "El algoritmo de X tiene ventajas y desventajas, como todo",
    "Habría más transparencia sobre cómo funciona la IA en redes sociales"
]


def obtener_tweets_reales(keyword, max_tweets=25):
    """Obtiene tweets reales usando birdapi con tus cookies."""
    
    if not BIRD_AVAILABLE:
        print(f"birdapi no disponible")
        return None
    
    # Verificar que pusiste tus cookies
    if MI_AUTH_TOKEN == "fe0c82afb85c50c95bac4e3c8dfc2b685ad7b701" or MI_CT0 == "7f64efdadfd7b4531eae0d1e6b9e2cac1323dc8bb0b62ac29428abb2a8c6fe9de2d449ba66548eaaf3a4940f73b4837ec9a045af542d464e514066441ea665487f358d9ff8bc0bf093b399c08be9f34a":
        print(f"No has configurado tus cookies en twitter_collector.py")
        print(f"Edita el archivo y pega tus auth_token y ct0")
        return None
    
    try:
        client = TwitterClient(auth_token=MI_AUTH_TOKEN, ct0=MI_CT0)
        
        # Buscar tweets en español, sin retweets
        tweets, next_cursor = client.search(
            f"{keyword} lang:es -is:retweet",
            count=min(max_tweets, 25)  # Máximo 25 por búsqueda
        )
        
        if not tweets:
            print(f" No se encontraron tweets para '{keyword}'")
            return None
        
        data = []
        for tweet in tweets:
            # Manejar la fecha
            try:
                if hasattr(tweet, 'created_at') and tweet.created_at:
                    fecha = tweet.created_at
                    if isinstance(fecha, str):
                        fecha = datetime.fromisoformat(fecha.replace('Z', '+00:00'))
                    fecha_str = fecha.strftime("%Y-%m-%d")
                else:
                    fecha_str = datetime.now().strftime("%Y-%m-%d")
            except:
                fecha_str = datetime.now().strftime("%Y-%m-%d")
            
            # Obtener nombre de usuario
            usuario = "unknown"
            if hasattr(tweet, 'author'):
                if hasattr(tweet.author, 'username'):
                    usuario = tweet.author.username
                elif hasattr(tweet.author, 'screen_name'):
                    usuario = tweet.author.screen_name
            
            data.append({
                'fecha': fecha_str,
                'red_social': 'X/Twitter',
                'usuario': usuario,
                'tema': keyword,
                'texto': tweet.text[:280] if hasattr(tweet, 'text') else str(tweet)[:280],
                'fuente': 'real_API'
            })
        
        if data:
            print(f"{len(data)} tweets REALES obtenidos de X/Twitter")
            return pd.DataFrame(data)
        else:
            return None
        
    except Exception as e:
        print(f"Error al obtener tweets reales: {e}")
        return None


def generar_tweets_simulados(keyword, cantidad=25):
    """Genera tweets simulados cuando la API real no funciona."""
    print(f"Generando {cantidad} tweets SIMULADOS para X/Twitter")
    
    data = []
    for i in range(cantidad):
        fecha = (datetime.now() - timedelta(days=random.randint(0, 7))).strftime("%Y-%m-%d")
        
        rand = random.random()
        if rand < 0.45:
            texto = random.choice(TWEETS_POSITIVOS)
        elif rand < 0.80:
            texto = random.choice(TWEETS_NEGATIVOS)
        else:
            texto = random.choice(TWEETS_NEUTRALES)
        
        # Personalizar el texto con el keyword
        texto = texto.replace("X/Twitter", f"{keyword} en X")
        
        data.append({
            'fecha': fecha,
            'red_social': 'X/Twitter',
            'usuario': f'user_{random.randint(1000, 9999)}',
            'tema': keyword,
            'texto': texto,
            'fuente': 'simulada'
        })
    
    return pd.DataFrame(data)


def get_twitter_data(keyword, max_tweets=25):
    """
    Función principal: intenta tweets reales, si falla usa simulados.
    """
    print(f"Conectando a X/Twitter...")
    
    # Intentar obtener tweets reales
    tweets_reales = obtener_tweets_reales(keyword, max_tweets)
    
    if tweets_reales is not None and not tweets_reales.empty:
        return tweets_reales
    else:
        print(f"Usando tweets SIMULADOS como respaldo")
        return generar_tweets_simulados(keyword, max_tweets)