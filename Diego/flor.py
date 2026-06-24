import streamlit as st
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Circle, Ellipse
import matplotlib.patches as mpatches
from io import BytesIO

# --- Configuración ---
st.set_page_config(
    page_title="🌸 Creador de Flores",
    page_icon="🌸",
    layout="centered"
)

# --- Estilo CSS ---
st.markdown("""
<style>
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
    
    .stColorPicker > div > div {
        border-radius: 20px !important;
        border: 2px solid #f472b6 !important;
    }
    
    .stSlider > div > div > div {
        background: linear-gradient(to right, #f472b6, #e879f9) !important;
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
</style>
""", unsafe_allow_html=True)

# --- Funciones ---
def fig_to_bytes(fig):
    buf = BytesIO()
    fig.savefig(buf, format="png", dpi=300, bbox_inches='tight', facecolor=fig.get_facecolor())
    buf.seek(0)
    return buf

def dibujar_flor(color_petalos, color_centro, color_tallo, color_hojas, color_fondo, tamaño, nombre):
    """Dibuja la flor con los colores personalizados"""
    fig, ax = plt.subplots(figsize=(10, 10))
    
    # Configurar fondo
    ax.set_facecolor(color_fondo)
    fig.patch.set_facecolor(color_fondo)
    
    # Centro de la flor
    centro_x, centro_y = 5, 5
    
    # --- Tallo ---
    tallo = plt.Line2D([centro_x, centro_x], [centro_y-1, centro_y-4], 
                       color=color_tallo, linewidth=8*tamaño, solid_capstyle='round')
    ax.add_line(tallo)
    
    # --- Hojas ---
    # Hoja izquierda
    hoja_izq = mpatches.Ellipse((centro_x-0.8, centro_y-2.5), 0.8*tamaño, 1.5*tamaño, 
                                angle=30, facecolor=color_hojas, edgecolor='none', alpha=0.9)
    ax.add_patch(hoja_izq)
    
    # Hoja derecha
    hoja_der = mpatches.Ellipse((centro_x+0.8, centro_y-2.5), 0.8*tamaño, 1.5*tamaño, 
                                angle=-30, facecolor=color_hojas, edgecolor='none', alpha=0.9)
    ax.add_patch(hoja_der)
    
    # --- Pétalos (capa externa) ---
    num_petalos = 8
    angulos = np.linspace(0, 2*np.pi, num_petalos, endpoint=False)
    
    for angulo in angulos:
        x_petalo = centro_x + 1.5 * tamaño * np.cos(angulo)
        y_petalo = centro_y + 1.5 * tamaño * np.sin(angulo)
        
        petalo = mpatches.Ellipse((x_petalo, y_petalo), 
                                 1.0*tamaño, 0.8*tamaño, 
                                 angle=np.degrees(angulo), 
                                 facecolor=color_petalos, 
                                 edgecolor='white', 
                                 linewidth=1, 
                                 alpha=0.9)
        ax.add_patch(petalo)
    
    # --- Petalos (capa interna) ---
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
    
    # --- Centro de la flor ---
    centro = Circle((centro_x, centro_y), 1.0*tamaño, 
                    facecolor=color_centro, edgecolor='white', linewidth=2)
    ax.add_patch(centro)
    
    centro_int = Circle((centro_x, centro_y), 0.5*tamaño, 
                        facecolor='white', edgecolor='none', alpha=0.3)
    ax.add_patch(centro_int)
    
    # --- Texto con el nombre ---
    ax.text(centro_x, centro_y-4.8, f"🌺 Para: {nombre}", 
            fontsize=18, fontweight='bold', 
            ha='center', va='center', 
            color='#333333',
            fontfamily='serif')
    
    # --- Ajustes finales ---
    ax.set_xlim([centro_x-4, centro_x+4])
    ax.set_ylim([centro_y-5, centro_y+4])
    ax.set_aspect('equal')
    ax.axis('off')
    
    return fig

# --- Inicializar estado ---
if 'paso' not in st.session_state:
    st.session_state.paso = 1

# --- Nombre ---
NOMBRE_PREDEFINIDO = "Fernanda"  # Cambia este nombre por el que quieras

# --- Imterfaz ---
st.title("🌸 Creador de Flores Personalizadas")

# --- PASO 1: Mensaje de bienvenida ---
if st.session_state.paso == 1:
    st.markdown(f"""
    <div class="welcome-message">
        🌸 ¡Hola! Esta flor es para {NOMBRE_PREDEFINIDO} 🌸
    </div>
    """, unsafe_allow_html=True)
    
    st.markdown("### ✨ Personaliza cada detalle")
    st.caption("Elige colores, ajusta el tamaño y crea una flor única")
    
    if st.button("🌟 Comenzar a crear", use_container_width=True):
        st.session_state.paso = 2
        st.rerun()

# --- PASO 2: Personalizacion ---
elif st.session_state.paso == 2:
    st.markdown(f"### 🎨 Personaliza la flor para {NOMBRE_PREDEFINIDO}")
    
    # --- Controles en columnas ---
    col1, col2 = st.columns(2)
    
    with col1:
        st.markdown("#### 🌸 Pétalos")
        color_petalos = st.color_picker("Color de pétalos", "#FF6B6B")
        
        st.markdown("#### 🌼 Centro")
        color_centro = st.color_picker("Color del centro", "#FFD93D")
        
        st.markdown("#### 🌿 Tallo")
        color_tallo = st.color_picker("Color del tallo", "#6BCB77")
    
    with col2:
        st.markdown("#### 🍃 Hojas")
        color_hojas = st.color_picker("Color de hojas", "#4CAF50")
        
        st.markdown("#### 🎨 Fondo")
        color_fondo = st.color_picker("Color de fondo", "#F0F4F8")
        
        st.markdown("#### 📏 Tamaño")
        tamaño = st.slider("Tamaño de la flor", 0.5, 2.0, 1.0, 0.1)
    
    # --- Mostrar nombre ---
    st.markdown("---")
    st.caption(f"💝 La flor será dedicada a: **{NOMBRE_PREDEFINIDO}**")
    
    # --- Botones ---
    col1, col2 = st.columns(2)
    with col1:
        if st.button("⬅ Atrás", use_container_width=True):
            st.session_state.paso = 1
            st.rerun()
    with col2:
        if st.button("🌸 Generar Flor", use_container_width=True):
            st.session_state.color_petalos = color_petalos
            st.session_state.color_centro = color_centro
            st.session_state.color_tallo = color_tallo
            st.session_state.color_hojas = color_hojas
            st.session_state.color_fondo = color_fondo
            st.session_state.tamaño = tamaño
            st.session_state.paso = 3
            st.rerun()

# --- PASO 3: Flor generada ---
elif st.session_state.paso == 3:
    st.markdown(f"### 🌸 Flor para {NOMBRE_PREDEFINIDO}")
    
    with st.spinner("Creando tu flor personalizada..."):
        fig = dibujar_flor(
            st.session_state.color_petalos,
            st.session_state.color_centro,
            st.session_state.color_tallo,
            st.session_state.color_hojas,
            st.session_state.color_fondo,
            st.session_state.tamaño,
            NOMBRE_PREDEFINIDO
        )
        
        # Mostrar la flor
        st.pyplot(fig, use_container_width=True)
        
        # Mensaje de exito
        st.success(f"🌸 ¡Flor creada exitosamente para {NOMBRE_PREDEFINIDO}!")
        
        # --- Botones de accion ---
        col1, col2, col3 = st.columns(3)
        with col1:
            st.download_button(
                label="📥 Descargar",
                data=fig_to_bytes(fig),
                file_name=f"flor_para_{NOMBRE_PREDEFINIDO}.png",
                mime="image/png",
                use_container_width=True
            )
        with col2:
            if st.button(" Editar", use_container_width=True):
                st.session_state.paso = 2
                st.rerun()
        with col3:
            if st.button(" Nueva flor", use_container_width=True):
                for key in list(st.session_state.keys()):
                    del st.session_state[key]
                st.rerun()
        
        # --- Mensaje para compartir ---
        st.markdown("---")
        st.markdown("### Comparte tu flor")
        
        mensaje_compartir = f"""🌸 ¡Mira esta hermosa flor para {NOMBRE_PREDEFINIDO}!
        
💝 Creada especialmente para ti con mucho cariño.

¡Espero que te guste! 💕"""
        
        st.text_area("Copia este mensaje:", mensaje_compartir, height=100)
        
        st.balloons()

# --- Sidebar ---
with st.sidebar:
    st.markdown("###  Progreso")
    pasos = [" Bienvenida", " Personalizar", " Flor"]
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
        st.markdown("###  Para")
        st.info(f"**{NOMBRE_PREDEFINIDO}**")
    
    if st.session_state.paso == 3:
        st.markdown("### 🎨 Colores usados")
        st.caption(f"🌸 Pétalos: {st.session_state.color_petalos}")
        st.caption(f"⭐ Centro: {st.session_state.color_centro}")
        st.caption(f"🌿 Tallo: {st.session_state.color_tallo}")
        st.caption(f"🍃 Hojas: {st.session_state.color_hojas}")
        st.caption(f"📏 Tamaño: {st.session_state.tamaño}")
    
    st.markdown("---")
    st.markdown("### Tips")
    st.info("""
    🌸 Personaliza colores
    📏 Ajusta el tamaño
    💝 Dedicada a alguien especial
    📥 Descarga en alta calidad
    """)

# --- Footer ---
st.markdown("---")
st.caption("💝 Hecho con amor para alguien especial")
