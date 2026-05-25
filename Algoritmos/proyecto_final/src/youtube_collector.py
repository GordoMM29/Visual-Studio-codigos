from googleapiclient.discovery import build
import pandas as pd
from .config import YOUTUBE_API_KEY

def get_youtube_comments(keyword, max_comments_por_video=20):
    """Extrae comentarios reales de YouTube sobre un tema."""
    youtube = build('youtube', 'v3', developerKey=YOUTUBE_API_KEY)
    
    # Buscar videos relacionados al tema
    search_response = youtube.search().list(
        q=keyword,
        part='id',
        maxResults=5,
        type='video'
    ).execute()
    
    video_ids = [item['id']['videoId'] for item in search_response.get('items', [])]
    
    all_comments = []
    for video_id in video_ids:
        try:
            comments_response = youtube.commentThreads().list(
                part='snippet',
                videoId=video_id,
                maxResults=max_comments_por_video,
                textFormat='plainText'
            ).execute()
            
            for item in comments_response.get('items', []):
                comment = item['snippet']['topLevelComment']['snippet']
                all_comments.append({
                    'fecha': comment['publishedAt'][:10],
                    'red_social': 'YouTube',
                    'usuario': comment['authorDisplayName'],
                    'tema': keyword,
                    'texto': comment['textDisplay']
                })
        except Exception as e:
            print(f"Error en video {video_id}: {e}")
    
    return pd.DataFrame(all_comments)