import streamlit as st
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Circle, Rectangle, FancyBboxPatch, Arc
from io import BytesIO
from datetime import datetime, timedelta
import random

# --- Configuración ---
st.set_page_config(
    page_title="💝 ¡Invitación Especial!",
    page_icon="💌",
    layout="centered"
)

# --- Funciones Auxiliares ---
def fig_to_bytes(fig):
    buf = BytesIO()
    fig.savefig(buf, format="png", dpi=300, bbox_inches='tight', facecolor=fig.get_facecolor())
    buf.seek(0)
    return buf

# --- FUNCIÓN PARA CREAR LA INVITACIÓN ---
def crear_invitacion(nombre_invitado, lugar, fecha, hora, tema, mensaje_personal):
    
    # Paleta de colores por defecto (elegante y romántica)
    colores = {
        'principal': '#E74C8B',
        'secundario': '#FF6B6B',
        'fondo': '#FFF5F7',
        'texto': '#2C3E50',
        'detalles': '#34495E'
    }
    
    fig, ax = plt.subplots(figsize=(10, 12))
    ax.set_facecolor(colores['fondo'])
    fig.patch.set_facecolor(colores['fondo'])
    
    # --- Marco decorativo ---
    marco = FancyBboxPatch((0.5, 0.5), 9, 11, 
                           boxstyle="round,pad=0.5", 
                           facecolor='white', 
                           edgecolor=colores['principal'], 
                           linewidth=3,
                           alpha=0.9)
    ax.add_patch(marco)
    
    # --- Decoración superior ---
    # Cinta decorativa
    cinta = Rectangle((1.5, 10.8), 7, 0.4, 
                      facecolor=colores['secundario'], 
                      alpha=0.7)
    ax.add_patch(cinta)
    
    # --- Título principal ---
    titulos = [
        "💝 ¡Una sorpresa te espera!",
        "✨ Tengo algo especial para ti",
        "🌟 ¿Quieres acompañarme?",
        "💕 ¡Esta invitación es para ti!",
        "🌹 Una cita especial te espera"
    ]
    titulo = random.choice(titulos)
    
    ax.text(5, 10.2, titulo, 
            fontsize=22, fontweight='bold', ha='center', va='center', 
            color=colores['principal'])
    
    # --- Corazón decorativo ---
    dibujar_corazon(ax, 5, 8.5, 0.6, colores['principal'], alpha=0.2)
    dibujar_corazon(ax, 5, 8.5, 0.4, colores['secundario'], alpha=0.3)
    
    # --- Mensaje principal ---
    ax.text(5, 7.5, f"Querid{'a' if nombre_invitado.endswith('a') else 'o'} {nombre_invitado},", 
            fontsize=20, fontweight='bold', ha='center', va='center', 
            color=colores['texto'])
    
    # --- Mensaje personalizado ---
    mensaje = mensaje_personal if mensaje_personal else f"Me encantaría compartir este momento contigo. He preparado algo especial y me gustaría mucho que estés allí."
    
    # Ajustar texto largo con saltos de línea
    if len(mensaje) > 60:
        mensaje = mensaje[:60] + "\n" + mensaje[60:]
    
    ax.text(5, 6.5, mensaje, 
            fontsize=15, ha='center', va='center', 
            color=colores['detalles'], linespacing=1.6)
    
    # --- Detalles de la cita (con iconos) ---
    detalles_y = 5.0
    
    # Fecha
    ax.text(5, detalles_y, "📅", fontsize=22, ha='center', va='center')
    ax.text(5, detalles_y-0.4, f"{fecha}", 
            fontsize=16, ha='center', va='center', color=colores['texto'])
    
    # Hora
    ax.text(5, detalles_y-1.0, "🕒", fontsize=22, ha='center', va='center')
    ax.text(5, detalles_y-1.4, f"{hora}", 
            fontsize=16, ha='center', va='center', color=colores['texto'])
    
    # Lugar
    ax.text(5, detalles_y-2.0, "📍", fontsize=22, ha='center', va='center')
    ax.text(5, detalles_y-2.4, f"{lugar}", 
            fontsize=16, ha='center', va='center', color=colores['texto'])
    
    # --- Elementos decorativos según el tema ---
    if tema == "Romántico 🌹":
        # Rosas decorativas
        for i in range(3):
            x = 2 + i * 3
            dibujar_rosa(ax, x, 3.2, 0.3, colores['principal'])
        # Corazones pequeños
        for i in range(6):
            angulo = i * 60 + 30
            x = 5 + 2.5 * np.cos(np.radians(angulo))
            y = 3.2 + 0.5 * np.sin(np.radians(angulo))
            dibujar_corazon(ax, x, y, 0.15, colores['secundario'], alpha=0.5)
            
    elif tema == "Aventura 🗺️":
        # Mapa o brújula
        compass = Circle((5, 3.2), 0.8, facecolor='none', edgecolor=colores['principal'], linewidth=2)
        ax.add_patch(compass)
        ax.plot([5, 5], [2.4, 4.0], color=colores['principal'], linewidth=2)
        ax.plot([4.2, 5.8], [3.2, 3.2], color=colores['principal'], linewidth=2)
        ax.text(5, 3.2, "✦", fontsize=20, ha='center', va='center', color=colores['secundario'])
        
        # Estrellas decorativas
        for i in range(5):
            x = 1.5 + i * 1.8
            y = 3.8 + 0.3 * np.sin(i * 2)
            ax.text(x, y, "★", fontsize=12, ha='center', va='center', color=colores['principal'], alpha=0.4)
            
    else:  # Divertido 🎉
        # Confeti y globos
        for _ in range(40):
            x = random.uniform(1.5, 8.5)
            y = random.uniform(1.5, 3.5)
            size = random.uniform(0.02, 0.06)
            color = random.choice([colores['principal'], colores['secundario'], '#FF6B6B', '#4ECDC4', '#FFD93D'])
            ax.scatter(x, y, s=size*2000, c=color, alpha=0.6)
        
        # Globos
        colores_globos = ['#FF6B6B', '#4ECDC4', '#FFD93D', '#A29BFE']
        for i, color in enumerate(colores_globos):
            x = 2 + i * 2
            ax.text(x, 3.5, "🎈", fontsize=25, ha='center', va='center', alpha=0.7)
    
    # --- Pie de página ---
    frases = [
        "Con mucho cariño ✨",
        "Espero que digas que sí 💕",
        "¡Será un momento inolvidable! 🌟",
        "Te espero con ilusión 💝"
    ]
    ax.text(5, 1.2, random.choice(frases), 
            fontsize=14, style='italic', ha='center', va='center', 
            color=colores['secundario'])
    
    # --- Botones de respuesta (interactivos visualmente) ---
    # Botón "Sí"
    ax.text(3.5, 0.4, "💚 ¡Sí, acepto!", 
            fontsize=14, fontweight='bold', ha='center', va='center', 
            color='white', 
            bbox=dict(boxstyle="round,pad=0.4", facecolor=colores['principal'], alpha=0.9))
    
    # Botón "No"
    ax.text(6.5, 0.4, "💔 No puedo", 
            fontsize=14, fontweight='bold', ha='center', va='center', 
            color='white', 
            bbox=dict(boxstyle="round,pad=0.4", facecolor='#95A5A6', alpha=0.8))
    
    ax.set_xlim([0, 10])
    ax.set_ylim([0, 12])
    ax.axis('off')
    plt.tight_layout()
    return fig

def dibujar_corazon(ax, x, y, size, color, alpha=1.0):
    t = np.linspace(0, 2*np.pi, 100)
    x_heart = x + size * (16 * np.sin(t)**3)
    y_heart = y + size * (13*np.cos(t) - 5*np.cos(2*t) - 2*np.cos(3*t) - np.cos(4*t))
    ax.fill(x_heart, y_heart, color=color, alpha=alpha, edgecolor='none')

def dibujar_rosa(ax, x, y, size, color):
    """Dibuja una rosa simple"""
    for i in range(5):
        angulo = i * 72 + 36
        radio = size * (0.5 + 0.3 * np.sin(np.radians(i * 72)))
        petalo = Circle((x + radio * np.cos(np.radians(angulo)), 
                        y + radio * np.sin(np.radians(angulo))), 
                       size*0.4, color=color, alpha=0.6)
        ax.add_patch(petalo)
    centro = Circle((x, y), size*0.2, color='white', alpha=0.8)
    ax.add_patch(centro)

# --- INTERFAZ DE STREAMLIT ---
st.title("💌 Crea una Invitación Especial")
st.markdown("Personaliza cada detalle y sorprende a esa persona especial")

# --- Inicializar estado de sesión ---
if 'paso' not in st.session_state:
    st.session_state.paso = 1

# --- PASO 1: ¿A quién invitas? ---
if st.session_state.paso == 1:
    st.header("Como te llamas persona bonita?")
    
    nombre_invitado = st.text_input("Nombre de la persona:")
    
    col1, col2 = st.columns([3, 1])
    with col2:
        if st.button("Siguiente ➜", use_container_width=True):
            if nombre_invitado:
                st.session_state.nombre_invitado = nombre_invitado
                st.session_state.paso = 2
                st.rerun()
            else:
                st.warning("Por favor, escribe un nombre")

# --- PASO 2: Planea la cita ---
elif st.session_state.paso == 2:
    st.header("📝 Planea los detalles")
    st.caption("Elige los detalles de la cita que le propondrás")
    
    col1, col2 = st.columns(2)
    with col1:
        lugar = st.text_input("📍 Lugar:", "Cafetería La Esquina")
        fecha = st.date_input("📅 Fecha:", datetime.now() + timedelta(days=7))
    with col2:
        hora = st.time_input("🕒 Hora:", datetime.now().replace(hour=19, minute=0))
        tema = st.selectbox("🎨 Tema de la invitación:", 
                           ["Romántico 🌹", "Aventura 🗺️", "Divertido 🎉"])
    
    mensaje_personal = st.text_area("💬 Mensaje personal (opcional):", 
                                    height=80,
                                    placeholder="Escribe un mensaje especial...")
    
    col1, col2 = st.columns(2)
    with col1:
        if st.button("⬅ Atrás", use_container_width=True):
            st.session_state.paso = 1
            st.rerun()
    with col2:
        if st.button("✨ Crear Invitación", use_container_width=True):
            st.session_state.lugar = lugar
            st.session_state.fecha = fecha
            st.session_state.hora = hora
            st.session_state.tema = tema
            st.session_state.mensaje_personal = mensaje_personal
            st.session_state.paso = 3
            st.rerun()

# --- PASO 3: Ver la invitación ---
elif st.session_state.paso == 3:
    st.header("💝 ¡Tu invitación está lista!")
    st.caption(f"Para: {st.session_state.nombre_invitado}")
    
    fecha_str = st.session_state.fecha.strftime("%d de %B de %Y")
    hora_str = st.session_state.hora.strftime("%I:%M %p")
    
    with st.spinner("Creando tu invitación..."):
        fig = crear_invitacion(
            st.session_state.nombre_invitado,
            st.session_state.lugar,
            fecha_str,
            hora_str,
            st.session_state.tema,
            st.session_state.mensaje_personal
        )
        
        st.pyplot(fig)
        
        # --- Acciones ---
        st.markdown("---")
        col1, col2, col3 = st.columns(3)
        
        with col1:
            st.download_button(
                label="📥 Descargar",
                data=fig_to_bytes(fig),
                file_name=f"invitacion_{st.session_state.nombre_invitado}.png",
                mime="image/png",
                use_container_width=True
            )
        
        with col2:
            if st.button("✏️ Editar detalles", use_container_width=True):
                st.session_state.paso = 2
                st.rerun()
        
        with col3:
            if st.button("🔄 Nueva invitación", use_container_width=True):
                # Resetear todo
                for key in list(st.session_state.keys()):
                    del st.session_state[key]
                st.rerun()
        
        # --- Compartir ---
        st.markdown("---")
        st.markdown("### 📱 Comparte tu invitación")
        
        mensaje_compartir = f"""💝 ¡Hola {st.session_state.nombre_invitado}!
        
Te invito a una cita especial:

📅 {fecha_str}
🕒 {hora_str}
📍 {st.session_state.lugar}

{st.session_state.mensaje_personal if st.session_state.mensaje_personal else "Me encantaría compartir este momento contigo."}

¿Aceptas la invitación? ¡Espero que sí! 💕"""
        
        st.text_area("📋 Copia este mensaje para compartir:", mensaje_compartir, height=150)
        
        if st.button("💬 Enviar mensaje", use_container_width=True):
            st.success(f"✅ ¡Mensaje preparado para {st.session_state.nombre_invitado}!")
            st.balloons()

# --- Información en sidebar ---
with st.sidebar:
    st.markdown("### 📍 Progreso")
    pasos = ["👋 Invitado/a", "📝 Detalles", "💝 Invitación"]
    paso_actual = st.session_state.paso - 1
    
    for i, paso in enumerate(pasos):
        if i < paso_actual:
            st.markdown(f"✅ {paso}")
        elif i == paso_actual:
            st.markdown(f"➡️ **{paso}**")
        else:
            st.markdown(f"⏳ {paso}")
    
    if st.session_state.paso >= 3:
        st.markdown("---")
        st.markdown("### 👤 Invitado/a")
        st.info(f"**{st.session_state.nombre_invitado}**")
        
        st.markdown("### 🎨 Tema")
        st.info(st.session_state.tema)
    
    st.markdown("---")
    st.markdown("### 💡 Consejos")
    st.info("""
    - Sé creativo con los detalles
    - Elige un lugar que le guste
    - Personaliza el mensaje
    - ¡Hazlo especial!
    """)

# --- Estilo CSS ---
st.markdown("""
<style>
    .stButton > button {
        background-color: #E74C8B;
        color: white;
        font-weight: bold;
        border-radius: 20px;
        padding: 10px 20px;
        border: none;
        transition: all 0.3s;
    }
    .stButton > button:hover {
        background-color: #C0392B;
        transform: scale(1.02);
    }
    .stTextInput > div > div > input {
        border-radius: 10px;
    }
    .stSelectbox > div > div {
        border-radius: 10px;
    }
    .stTextArea > div > div > textarea {
        border-radius: 10px;
    }
    [data-testid="stHeader"] {
        background-color: transparent;
    }
</style>
""", unsafe_allow_html=True)