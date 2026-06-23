import streamlit as st
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Polygon, Circle, Ellipse
import matplotlib.patches as mpatches
from io import BytesIO

# Configuración de la página
st.set_page_config(
    page_title="Flor Personalizada",
    page_icon="🌺",
    layout="centered"
)

# Título y descripción
st.title("🌺 Creador de Flores Personalizadas")
st.markdown("---")

# Sidebar para controles
with st.sidebar:
    st.header("🎨 Personaliza tu flor")
    
    # Selección de colores
    color_petalos = st.color_picker("Color de los pétalos", "#FF6B6B")
    color_centro = st.color_picker("Color del centro", "#FFD93D")
    color_tallo = st.color_picker("Color del tallo", "#6BCB77")
    color_hojas = st.color_picker("Color de las hojas", "#4CAF50")
    color_fondo = st.color_picker("Color de fondo", "#F0F4F8")
    
    # Tamaño de la flor
    tamaño = st.slider("Tamaño de la flor", 0.5, 2.0, 1.0, 0.1)
    
    st.markdown("---")
    
    # Nombre para quien va dedicada (definido en código)
    nombre = st.text_input("Nombre para quien va dedicada", "nombre de la persona")
    
    # Mensaje adicional
    mensaje = st.text_area("Mensaje adicional", "¡Con mucho cariño!")

# Función para convertir la figura a bytes (DEFINIDA ANTES DE USARSE)
def fig_to_bytes(fig):
    buf = BytesIO()
    fig.savefig(buf, format="png", dpi=300, bbox_inches='tight', facecolor=fig.get_facecolor())
    buf.seek(0)
    return buf

# Función para dibujar la flor
def dibujar_flor(color_petalos, color_centro, color_tallo, color_hojas, color_fondo, tamaño, nombre, mensaje):
    fig, ax = plt.subplots(figsize=(10, 10))
    
    # Configurar el fondo
    ax.set_facecolor(color_fondo)
    fig.patch.set_facecolor(color_fondo)
    
    # Definir el centro de la flor
    centro_x, centro_y = 5, 5
    
    # Dibujar tallo
    tallo = plt.Line2D([centro_x, centro_x], [centro_y-1, centro_y-4], 
                       color=color_tallo, linewidth=8*tamaño, solid_capstyle='round')
    ax.add_line(tallo)
    
    # Dibujar hojas
    # Hoja izquierda
    hoja_izq = mpatches.Ellipse((centro_x-0.8, centro_y-2.5), 0.8*tamaño, 1.5*tamaño, 
                                angle=30, facecolor=color_hojas, edgecolor='none', alpha=0.9)
    ax.add_patch(hoja_izq)
    
    # Hoja derecha
    hoja_der = mpatches.Ellipse((centro_x+0.8, centro_y-2.5), 0.8*tamaño, 1.5*tamaño, 
                                angle=-30, facecolor=color_hojas, edgecolor='none', alpha=0.9)
    ax.add_patch(hoja_der)
    
    # Dibujar pétalos
    num_petalos = 8
    angulos = np.linspace(0, 2*np.pi, num_petalos, endpoint=False)
    
    for angulo in angulos:
        # Posición del pétalo
        x_petalo = centro_x + 1.5 * tamaño * np.cos(angulo)
        y_petalo = centro_y + 1.5 * tamaño * np.sin(angulo)
        
        # Crear forma de pétalo (elipse)
        petalo = mpatches.Ellipse((x_petalo, y_petalo), 
                                 1.0*tamaño, 0.8*tamaño, 
                                 angle=np.degrees(angulo), 
                                 facecolor=color_petalos, 
                                 edgecolor='white', 
                                 linewidth=1, 
                                 alpha=0.9)
        ax.add_patch(petalo)
    
    # Pétalos interiores (capa adicional para más detalle)
    for angulo in angulos:
        x_petalo = centro_x + 1.0 * tamaño * np.cos(angulo + np.pi/num_petalos)
        y_petalo = centro_y + 1.0 * tamaño * np.sin(angulo + np.pi/num_petalos)
        
        petalo_int = mpatches.Ellipse((x_petalo, y_petalo), 
                                     0.7*tamaño, 0.5*tamaño, 
                                     angle=np.degrees(angulo + np.pi/num_petalos), 
                                     facecolor=color_petalos, 
                                     edgecolor='white', 
                                     linewidth=1, 
                                     alpha=0.7)
        ax.add_patch(petalo_int)
    
    # Dibujar centro de la flor
    centro = Circle((centro_x, centro_y), 1.0*tamaño, 
                    facecolor=color_centro, edgecolor='white', linewidth=2)
    ax.add_patch(centro)
    
    # Añadir círculo interior al centro
    centro_int = Circle((centro_x, centro_y), 0.5*tamaño, 
                        facecolor='white', edgecolor='none', alpha=0.3)
    ax.add_patch(centro_int)
    
    # Ajustar límites
    ax.set_xlim([centro_x-4, centro_x+4])
    ax.set_ylim([centro_y-5, centro_y+4])
    ax.set_aspect('equal')
    ax.axis('off')
    
    # Añadir texto con el nombre
    ax.text(centro_x, centro_y-4.8, f"Para: {nombre}", 
            fontsize=18, fontweight='bold', 
            ha='center', va='center', 
            color='#333333')
    
    # Añadir mensaje adicional
    if mensaje:
        ax.text(centro_x, centro_y-5.3, mensaje, 
                fontsize=14, style='italic',
                ha='center', va='center', 
                color='#666666')
    
    return fig

# Botón para generar la flor
if st.button("🌸 Generar Flor", use_container_width=True):
    with st.spinner("Creando tu flor personalizada..."):
        # Dibujar la flor
        fig = dibujar_flor(color_petalos, color_centro, color_tallo, color_hojas, 
                          color_fondo, tamaño, nombre, mensaje)
        
        # Mostrar la flor
        st.pyplot(fig)
        
        # Mensaje de éxito
        st.success(f"🌸 ¡Flor creada exitosamente para {nombre}!")
        
        # Opción para descargar
        st.download_button(
            label="📥 Descargar flor (PNG)",
            data=fig_to_bytes(fig),
            file_name=f"flor_para_{nombre}.png",
            mime="image/png",
            use_container_width=True
        )

# Instrucciones
st.markdown("---")
st.markdown("""
### 📝 Instrucciones:
1. **Personaliza los colores** de tu flor usando los selectores de color
2. **Ajusta el tamaño** con el control deslizante
3. **Escribe el nombre** de la persona a quien va dedicada
4. **Añade un mensaje** especial
5. **Haz clic en "Generar Flor"** para ver tu creación
6. **Descarga la imagen** en formato PNG
""")

# Información adicional
st.info("💡 La flor se genera con 16 pétalos en dos capas para un efecto más realista.")