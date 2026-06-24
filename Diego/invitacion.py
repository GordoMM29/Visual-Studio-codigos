import streamlit as st
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Circle, Ellipse, Rectangle
from io import BytesIO
from datetime import datetime, timedelta
import random
import textwrap

# --- Configuración ---
st.set_page_config(
    page_title="💝 Invitación Especial",
    page_icon="💌",
    layout="centered"
)

# --- Estilo CSS personalizado ---
st.markdown("""
<style>
    @import url('https://fonts.googleapis.com/css2?family=Dancing+Script:wght@600;700&family=Lato:wght@300;400&display=swap');
    
    .stApp {
        background: radial-gradient(ellipse at 60% 0%, #f70a84 0%, #d675a9 40%, #fce8ff 100%);
    }
    
    .main > div {
        background: rgba(255,255,255,0.85);
        backdrop-filter: blur(14px);
        border: 1.5px solid rgba(26, 1, 11, 0.2);
        border-radius: 2.5rem;
        padding: 2rem;
        max-width: 550px;
        margin: 2rem auto;
        box-shadow: 0 8px 40px rgba(220,80,130,0.15);
    }
    
    h1, h2, h3 {
        font-family: 'Dancing Script', cursive !important;
        color: #c0185a !important;
        text-align: center !important;
    }
    
    .stButton > button {
        background: linear-gradient(to right, #f472b6, #e879f9) !important;
        color: white !important;
        font-family: 'Dancing Script', cursive !important;
        font-size: 1.2rem !important;
        border-radius: 30px !important;
        border: none !important;
        padding: 0.75rem 2rem !important;
        transition: all 0.3s ease !important;
        box-shadow: 0 4px 15px rgba(228, 121, 249, 0.3) !important;
        width: 100% !important;
    }
    
    .stButton > button:hover {
        transform: scale(1.02) !important;
        box-shadow: 0 6px 20px rgba(228, 121, 249, 0.4) !important;
    }
    
    .stTextInput > div > div > input,
    .stTextArea > div > div > textarea,
    .stSelectbox > div > div,
    .stDateInput > div > div > input,
    .stTimeInput > div > div > input {
        border-radius: 20px !important;
        border: 2px solid #f472b6 !important;
        background: rgba(255,255,255,0.9) !important;
        font-family: 'Lato', sans-serif !important;
    }
    
    .element-container {
        margin-bottom: 0.8rem !important;
    }
    
    .welcome-message {
        font-family: 'Dancing Script', cursive;
        font-size: 1.8rem;
        color: #c0185a;
        text-align: center;
        padding: 1rem;
        background: linear-gradient(to right, #fce4ec, #f3e5f5);
        border-radius: 20px;
        margin: 1rem 0;
        border: 2px solid #f472b6;
    }
    
    .character-counter {
        text-align: right;
        font-size: 0.8rem;
        color: #888;
        margin-top: -0.5rem;
    }
</style>
""", unsafe_allow_html=True)

# --- Funciones ---
def fig_to_bytes(fig):
    buf = BytesIO()
    fig.savefig(buf, format="png", dpi=200, bbox_inches='tight', facecolor=fig.get_facecolor())
    buf.seek(0)
    return buf

def ajustar_texto(texto, max_caracteres=120):
    """Ajusta el texto para que no se salga del marco"""
    if len(texto) > max_caracteres:
        # Cortar el texto y añadir "..."
        return texto[:max_caracteres] + "..."
    return texto

def dividir_texto_en_lineas(texto, max_longitud=35):
    """Divide el texto en líneas de longitud máxima"""
    # Si el texto es muy corto, devolverlo como una sola línea
    if len(texto) <= max_longitud:
        return [texto]
    
    # Usar textwrap para dividir el texto
    lineas = textwrap.wrap(texto, width=max_longitud)
    
    # Limitar a máximo 3 líneas
    if len(lineas) > 3:
        lineas = lineas[:3]
        lineas[-1] = lineas[-1] + "..."
    
    return lineas

def dibujar_flor_default(ax, x, y, size=1.0):
    """Dibuja la flor con colores predeterminados"""
    
    color_petalos = "#f9a8d4"
    color_petalos_oscuro = "#f472b6"
    color_centro = "#fde68a"
    color_centro_oscuro = "#fbbf24"
    color_tallo = "#5a9e4e"
    color_hojas = "#7bc67a"
    
    # Tallo
    ax.plot([x, x], [y-0.5, y-2.5], color=color_tallo, linewidth=4*size, solid_capstyle='round')
    
    # Hojas
    hoja = Ellipse((x-0.4*size, y-1.8*size), 0.5*size, 0.3*size, 
                   angle=-30, facecolor=color_hojas, edgecolor='none', alpha=0.9)
    ax.add_patch(hoja)
    
    hoja2 = Ellipse((x+0.4*size, y-1.6*size), 0.5*size, 0.3*size, 
                    angle=30, facecolor=color_hojas, edgecolor='none', alpha=0.9)
    ax.add_patch(hoja2)
    
    # Pétalos
    num_petalos = 8
    angulos = np.linspace(0, 2*np.pi, num_petalos, endpoint=False)
    
    for i, angulo in enumerate(angulos):
        px = x + 0.7 * size * np.cos(angulo)
        py = y + 0.7 * size * np.sin(angulo)
        
        color = color_petalos if i % 2 == 0 else color_petalos_oscuro
        
        petalo = Ellipse((px, py), 0.4*size, 0.7*size, 
                         angle=np.degrees(angulo), 
                         facecolor=color, 
                         edgecolor='white', 
                         linewidth=0.5, 
                         alpha=0.85)
        ax.add_patch(petalo)
    
    # Centro
    centro1 = Circle((x, y), 0.5*size, facecolor=color_centro, edgecolor='white', linewidth=1)
    ax.add_patch(centro1)
    
    centro2 = Circle((x, y), 0.35*size, facecolor=color_centro_oscuro, edgecolor='none', alpha=0.7)
    ax.add_patch(centro2)
    
    centro3 = Circle((x, y), 0.2*size, facecolor='#f59e0b', edgecolor='none', alpha=0.5)
    ax.add_patch(centro3)

def crear_invitacion(nombre_invitado, lugar, fecha, hora, mensaje):
    """Crea la tarjeta de invitación con texto ajustado"""
    fig, ax = plt.subplots(figsize=(7, 9))
    
    # Fondo suave
    ax.set_facecolor('#FFF5F7')
    fig.patch.set_facecolor('#FFF5F7')
    
    # Marco
    marco = Rectangle((0.2, 0.2), 6.6, 8.6, 
                      facecolor='white', 
                      edgecolor='#f472b6', 
                      linewidth=2,
                      alpha=0.9)
    ax.add_patch(marco)
    
    # Bordes redondeados
    for esquina in [(0.2, 0.2), (6.8, 0.2), (0.2, 8.8), (6.8, 8.8)]:
        circle = Circle(esquina, 0.3, facecolor='white', edgecolor='#f472b6', linewidth=2)
        ax.add_patch(circle)
    
    # Dibujar la flor
    dibujar_flor_default(ax, 3.5, 6.8, 1.0)
    
    # Texto "Para: [nombre]"
    ax.text(3.5, 5.6, f"Para: {nombre_invitado} 💕", 
            fontsize=20, fontweight='bold', ha='center', va='center',
            fontfamily='serif', color='#c0185a')
    
    # Línea divisoria
    ax.plot([1.5, 5.5], [5.3, 5.3], color='#f472b6', linewidth=1.5, alpha=0.5)
    
    # --- AJUSTE DE TEXTO DEL MENSAJE ---
    # Dividir el mensaje en líneas de máximo 35 caracteres
    lineas_mensaje = dividir_texto_en_lineas(mensaje, max_longitud=35)
    
    # Posición inicial del texto
    y_texto = 4.8
    
    # Dibujar cada línea del mensaje
    for linea in lineas_mensaje[:3]:  # Máximo 3 líneas
        ax.text(3.5, y_texto, linea, 
                fontsize=11, ha='center', va='center',
                fontfamily='sans-serif', color='#5a2040')
        y_texto -= 0.5
    
    # Si hay más de 3 líneas, añadir "..."
    if len(lineas_mensaje) > 3:
        ax.text(3.5, y_texto, "...", 
                fontsize=11, ha='center', va='center',
                fontfamily='sans-serif', color='#5a2040', style='italic')
        y_texto -= 0.3
    
    # Detalles de la cita (con ajuste de texto para lugar)
    y_detalles = y_texto - 0.3
    
    # Fecha
    ax.text(3.5, y_detalles, f" {fecha}", 
            fontsize=13, ha='center', va='center', color='#5a2040')
    
    # Hora
    ax.text(3.5, y_detalles-0.6, f" {hora}", 
            fontsize=13, ha='center', va='center', color='#5a2040')
    
    # Lugar (con ajuste si es muy largo)
    lugar_ajustado = ajustar_texto(lugar, max_caracteres=25)
    ax.text(3.5, y_detalles-1.2, f" {lugar_ajustado}", 
            fontsize=13, ha='center', va='center', color='#5a2040')
    
    # Línea divisoria inferior
    ax.plot([1.5, 5.5], [y_detalles-1.8, y_detalles-1.8], color='#f472b6', linewidth=1.5, alpha=0.5)
    
    # Nombre del remitente
    frases = ["Con amor", "Con cariño", "Con mucho cariño", "Para ti", "Siempre para ti"]
    ax.text(3.5, y_detalles-2.4, f"{random.choice(frases)} 💝", 
            fontsize=16, ha='center', va='center',
            fontfamily='serif', color='#a0185a', fontweight='bold', style='italic')
    
    ax.set_xlim([0, 7])
    ax.set_ylim([0, 9])
    ax.set_aspect('equal')
    ax.axis('off')
    
    return fig

# --- Inicializar estado ---
if 'paso' not in st.session_state:
    st.session_state.paso = 1

# Nombre 
NOMBRE_PREDEFINIDO = "Fernanda"

# --- INTERFAZ ---
st.title("💌 Invitación Especial")

# --- PASO 1: Bienvenida ---
if st.session_state.paso == 1:
    st.markdown(f"""
    <div class="welcome-message">
        💝 ¡Hola {NOMBRE_PREDEFINIDO}! 💝
    </div>
    """, unsafe_allow_html=True)
    
    st.markdown("### ✨ ¿Listo para planear la cita perfecta?")
    st.caption("Completa los detalles y crea una invitación única")
    
    if st.button("🌟 Comenzar a planear", use_container_width=True):
        st.session_state.paso = 2
        st.rerun()

# --- PASO 2: Planear la cita ---
elif st.session_state.paso == 2:
    st.markdown(f"###  Planea tu cita con {NOMBRE_PREDEFINIDO}")
    
    col1, col2 = st.columns(2)
    with col1:
        lugar = st.text_input(" ¿Dónde?", "Cafetería La Esquina")
        fecha = st.date_input(" ¿Cuándo?", datetime.now() + timedelta(days=7))
    with col2:
        hora = st.time_input(" ¿A qué hora?", datetime.now().replace(hour=19, minute=0))
    
    # Mensaje 
    mensaje_default = f"¡Hola {NOMBRE_PREDEFINIDO}! Me encantaría compartir este momento especial contigo."
    
    mensaje = st.text_area(
        " Mensaje personal (no escribas mas de 120 palabras profa jajajaja):",
        value=mensaje_default,
        height=80,
        help="Escribe un mensaje corto y especial"
    )
    
    # Contador de caracteres
    caracteres_restantes = 120 - len(mensaje)
    color_contador = "green" if caracteres_restantes >= 0 else "red"
    st.markdown(f"""
    <div class="character-counter" style="color: {color_contador};">
        {caracteres_restantes} caracteres restantes
    </div>
    """, unsafe_allow_html=True)
    
    # Advertencia si el mensaje es muy largo
    if len(mensaje) > 120:
        st.warning(" El mensaje es muy largo. Se truncará automáticamente en la invitación.")
    
    st.markdown("---")
    st.caption("🌸 Se genera un detalle para la invitacion.")
    st.caption("")
    
    col1, col2 = st.columns(2)
    with col1:
        if st.button("⬅ Atrás", use_container_width=True):
            st.session_state.paso = 1
            st.rerun()
    with col2:
        if st.button("💝 Crear Invitación", use_container_width=True):
            st.session_state.lugar = lugar
            st.session_state.fecha = fecha.strftime("%d de %B de %Y")
            st.session_state.hora = hora.strftime("%I:%M %p")
            st.session_state.mensaje = mensaje
            st.session_state.paso = 3
            st.rerun()

# --- PASO 3: Invitación final ---
elif st.session_state.paso == 3:
    st.markdown(f"### 💝 Invitación para {NOMBRE_PREDEFINIDO}")
    
    with st.spinner("Creando tu invitación..."):
        fig = crear_invitacion(
            NOMBRE_PREDEFINIDO,
            st.session_state.lugar,
            st.session_state.fecha,
            st.session_state.hora,
            st.session_state.mensaje
        )
        
        st.pyplot(fig, use_container_width=True)
        
        # Botones de acción
        col1, col2, col3 = st.columns(3)
        with col1:
            st.download_button(
                label=" Descargar",
                data=fig_to_bytes(fig),
                file_name=f"invitacion_{NOMBRE_PREDEFINIDO}.png",
                mime="image/png",
                use_container_width=True
            )
        with col2:
            if st.button(" Editar", use_container_width=True):
                st.session_state.paso = 2
                st.rerun()
        with col3:
            if st.button(" Nueva", use_container_width=True):
                for key in list(st.session_state.keys()):
                    del st.session_state[key]
                st.rerun()
        
        # Mensaje para compartir
        st.markdown("---")
        st.markdown("### 📱 Comparte tu invitación")
        
        mensaje_compartir = f"""💝 ¡Hola {NOMBRE_PREDEFINIDO}!

Te invito a una cita especial:

 {st.session_state.fecha}
 {st.session_state.hora}
 {st.session_state.lugar}

{st.session_state.mensaje}

¡Espero que puedas venir! 💕"""
        
        st.text_area(" Copia este mensaje:", mensaje_compartir, height=120)
        
        st.balloons()

# --- Sidebar ---
with st.sidebar:
    st.markdown("### Progreso")
    pasos = [" Bienvenida", " Planear", " Invitación"]
    paso_actual = st.session_state.paso - 1
    
    for i, paso in enumerate(pasos):
        if i < paso_actual:
            st.markdown(f" {paso}")
        elif i == paso_actual:
            st.markdown(f" **{paso}**")
        else:
            st.markdown(f" {paso}")
    
    if st.session_state.paso >= 2:
        st.markdown("---")
        st.markdown("### 👤 Para")
        st.info(f"**{NOMBRE_PREDEFINIDO}**")
    
    if st.session_state.paso == 3:
        st.markdown("### Detalles")
        st.caption(f" {st.session_state.lugar}")
        st.caption(f" {st.session_state.fecha}")
        st.caption(f" {st.session_state.hora}")
    
    st.markdown("---")
    st.markdown("### Tips")
    st.info("""
     Mensaje máximo: 120 caracteres
     El texto se ajusta automáticamente
     Diseño de flor predeterminado
     Descarga en alta calidad
    """)
