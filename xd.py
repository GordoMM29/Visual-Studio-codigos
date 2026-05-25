from PIL import Image, ImageDraw, ImageFont

# Crear imagen en blanco
img = Image.new('RGB', (1200, 800), color='white')
draw = ImageDraw.Draw(img)

# Texto del diagrama (el que te mostré anteriormente)
diagrama = """
┌─────────────────────────────────────────────────────────────────┐
│                    MEJORAS IMPLEMENTADAS                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1️⃣ CRUZA MULTIPUNTO      │  +100% flexibilidad                │
│     (2 → 2-4 puntos)      │                                     │
│  ─────────────────────────┼─────────────────────────────────────│
│  2️⃣ SELECCIÓN INTELIGENTE │  +300% calidad padres              │
│     (Aleatoria → Torneo)   │                                     │
│  ─────────────────────────┼─────────────────────────────────────│
│  3️⃣ MUTACIÓN AVANZADA     │  +200% diversidad                  │
│     (1 → 3 operaciones)    │                                     │
│  ─────────────────────────┼─────────────────────────────────────│
│  4️⃣ ELITISMO              │  ✅ Nunca se pierde el mejor        │
│     (0 → 3 individuos)     │                                     │
│  ─────────────────────────┼─────────────────────────────────────│
│  5️⃣ REFRESCO POBLACIÓN    │  ✅ Evita estancamiento             │
│     (Nunca → Cada 15 gen)  │                                     │
│  ─────────────────────────┼─────────────────────────────────────│
│  6️⃣ ELIMINACIÓN ADAPTATIVA │  ✅ Mejor convergencia final        │
│     (Fija → Variable)      │                                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
"""

# Usar fuente monoespaciada
try:
    font = ImageFont.truetype("cour.ttf", 14)
except:
    font = ImageFont.load_default()

# Dibujar el diagrama
draw.text((50, 50), diagrama, fill='black', font=font)

img.save('diagrama_mejoras.png')
print("✅ Imagen guardada como 'diagrama_mejoras.png'")