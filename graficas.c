// Modulo de graficas
// Modulo de graficas - Versión simple pero funcional

#include <gtk/gtk.h>
#include <sys/sysinfo.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define MAX_PUNTOS 60  // Últimos 60 segundos
#define MAX_RATE_KB 1024.0  // Escala de red en KB/s

typedef struct {
    GtkWidget *drawing_area;
    GtkWidget *drawing_area_red;
    double datos_memoria[MAX_PUNTOS];
    double datos_swap[MAX_PUNTOS];
    double datos_rx[MAX_PUNTOS];
    double datos_tx[MAX_PUNTOS];
    unsigned long rx_anterior;
    unsigned long tx_anterior;
    int posicion_actual;
} DatosMonitor;

// Obtener uso de memoria y swap (archivo de paginación)
void obtener_porcentajes(double *memoria, double *swap) {
    struct sysinfo info;
    
    if (sysinfo(&info) == 0) {
        // Memoria RAM
        unsigned long total_mem = info.totalram;
        unsigned long mem_usada = total_mem - info.freeram;
        *memoria = (double)mem_usada / total_mem * 100.0;
        
        // Swap (Page File)
        if (info.totalswap > 0) {
            unsigned long swap_usado = info.totalswap - info.freeswap;
            *swap = (double)swap_usado / info.totalswap * 100.0;
        } else {
            *swap = 0.0;
        }
    }
}

// Leer datos de red (total recibidos y transmitidos en bytes)
void leer_datos_red(unsigned long *rx, unsigned long *tx) {
    FILE *archivo = fopen("/proc/net/dev", "r");
    if (!archivo) {
        *rx = 0;
        *tx = 0;
        return;
    }

    char linea[256];
    // Saltar encabezados
    fgets(linea, sizeof(linea), archivo);
    fgets(linea, sizeof(linea), archivo);

    unsigned long total_rx = 0;
    unsigned long total_tx = 0;

    while (fgets(linea, sizeof(linea), archivo)) {
        char iface[64];
        unsigned long rx_bytes, tx_bytes;
        // Formato: iface: rx ... tx ...
        if (sscanf(linea, "%63s %lu %*u %*u %*u %*u %*u %*u %*u %lu", iface, &rx_bytes, &tx_bytes) == 3) {
            // Eliminar ':' del nombre de interfaz
            char *dos_puntos = strchr(iface, ':');
            if (dos_puntos) *dos_puntos = '\0';
            if (strcmp(iface, "lo") != 0) {
                total_rx += rx_bytes;
                total_tx += tx_bytes;
            }
        }
    }
    fclose(archivo);

    *rx = total_rx;
    *tx = total_tx;
}

// Dibujar la gráfica
gboolean dibujar_grafica(GtkWidget *widget, cairo_t *cr, gpointer data) {
    DatosMonitor *dm = (DatosMonitor*)data;
    int ancho = gtk_widget_get_allocated_width(widget);
    int alto = gtk_widget_get_allocated_height(widget);
    
    // Fondo blanco
    cairo_set_source_rgb(cr, 1, 1, 1);
    cairo_paint(cr);
    
    // Dibujar borde y cuadrícula simple
    cairo_set_source_rgb(cr, 0.8, 0.8, 0.8);
    cairo_set_line_width(cr, 1);
    
    // Líneas horizontales (0%, 50%, 100%)
    for (int i = 0; i <= 2; i++) {
        double y = alto - 20 - (i * (alto - 40) / 2);
        cairo_move_to(cr, 40, y);
        cairo_line_to(cr, ancho - 10, y);
        cairo_stroke(cr);
        
        // Texto de porcentaje
        char texto[10];
        sprintf(texto, "%d%%", i * 50);
        cairo_set_source_rgb(cr, 0, 0, 0);
        cairo_move_to(cr, 5, y + 5);
        cairo_show_text(cr, texto);
    }
    
    // Dibujar línea de memoria (azul)
    double paso_x = (double)(ancho - 50) / (MAX_PUNTOS - 1);
    cairo_set_source_rgb(cr, 0, 0.4, 0.8);
    cairo_set_line_width(cr, 2);
    
    int primero = 1;
    for (int i = 0; i < MAX_PUNTOS; i++) {
        int idx = (dm->posicion_actual + i) % MAX_PUNTOS;
        double x = 40 + i * paso_x;
        double y = alto - 20 - (dm->datos_memoria[idx] / 100.0) * (alto - 40);
        
        if (primero) {
            cairo_move_to(cr, x, y);
            primero = 0;
        } else {
            cairo_line_to(cr, x, y);
        }
    }
    cairo_stroke(cr);
    
    // Dibujar línea de swap (rojo)
    cairo_set_source_rgb(cr, 0.9, 0.2, 0.2);
    cairo_set_line_width(cr, 2);
    
    primero = 1;
    for (int i = 0; i < MAX_PUNTOS; i++) {
        int idx = (dm->posicion_actual + i) % MAX_PUNTOS;
        double x = 40 + i * paso_x;
        double y = alto - 20 - (dm->datos_swap[idx] / 100.0) * (alto - 40);
        
        if (primero) {
            cairo_move_to(cr, x, y);
            primero = 0;
        } else {
            cairo_line_to(cr, x, y);
        }
    }
    cairo_stroke(cr);
    
    // Leyenda simple
    cairo_set_font_size(cr, 10);
    cairo_set_source_rgb(cr, 0, 0.4, 0.8);
    cairo_move_to(cr, ancho - 80, 20);
    cairo_show_text(cr, "RAM");
    
    cairo_set_source_rgb(cr, 0.9, 0.2, 0.2);
    cairo_move_to(cr, ancho - 80, 40);
    cairo_show_text(cr, "SWAP");
    
    return FALSE;
}

// Dibujar gráfica de red (descarga y subida)
gboolean dibujar_grafica_red(GtkWidget *widget, cairo_t *cr, gpointer data) {
    DatosMonitor *dm = (DatosMonitor*)data;
    int ancho = gtk_widget_get_allocated_width(widget);
    int alto = gtk_widget_get_allocated_height(widget);

    // Fondo blanco
    cairo_set_source_rgb(cr, 1, 1, 1);
    cairo_paint(cr);

    // Líneas horizontales de referencia
    cairo_set_source_rgb(cr, 0.8, 0.8, 0.8);
    cairo_set_line_width(cr, 1);
    for (int i = 0; i <= 2; i++) {
        double y = alto - 20 - (i * (alto - 40) / 2);
        cairo_move_to(cr, 40, y);
        cairo_line_to(cr, ancho - 10, y);
        cairo_stroke(cr);

        char texto[32];
        sprintf(texto, "%d KB/s", i * 512);
        cairo_set_source_rgb(cr, 0, 0, 0);
        cairo_move_to(cr, 5, y + 5);
        cairo_show_text(cr, texto);
    }

    // Calcular escala según máximo actual
    double max_val = MAX_RATE_KB;
    for (int i = 0; i < MAX_PUNTOS; i++) {
        if (dm->datos_rx[i] > max_val) max_val = dm->datos_rx[i];
        if (dm->datos_tx[i] > max_val) max_val = dm->datos_tx[i];
    }
    if (max_val < 100.0) max_val = 100.0;

    double paso_x = (double)(ancho - 50) / (MAX_PUNTOS - 1);

    // Línea de descarga (verde)
    cairo_set_source_rgb(cr, 0.1, 0.6, 0.1);
    cairo_set_line_width(cr, 2);
    int primero = 1;
    for (int i = 0; i < MAX_PUNTOS; i++) {
        int idx = (dm->posicion_actual + i) % MAX_PUNTOS;
        double x = 40 + i * paso_x;
        double y = alto - 20 - (dm->datos_rx[idx] / max_val) * (alto - 40);
        if (primero) {
            cairo_move_to(cr, x, y);
            primero = 0;
        } else {
            cairo_line_to(cr, x, y);
        }
    }
    cairo_stroke(cr);

    // Línea de subida (naranja)
    cairo_set_source_rgb(cr, 0.9, 0.5, 0.1);
    cairo_set_line_width(cr, 2);
    primero = 1;
    for (int i = 0; i < MAX_PUNTOS; i++) {
        int idx = (dm->posicion_actual + i) % MAX_PUNTOS;
        double x = 40 + i * paso_x;
        double y = alto - 20 - (dm->datos_tx[idx] / max_val) * (alto - 40);
        if (primero) {
            cairo_move_to(cr, x, y);
            primero = 0;
        } else {
            cairo_line_to(cr, x, y);
        }
    }
    cairo_stroke(cr);

    // Leyenda
    cairo_set_font_size(cr, 10);
    cairo_set_source_rgb(cr, 0.1, 0.6, 0.1);
    cairo_move_to(cr, ancho - 120, 20);
    cairo_show_text(cr, "Descarga");
    cairo_set_source_rgb(cr, 0.9, 0.5, 0.1);
    cairo_move_to(cr, ancho - 120, 40);
    cairo_show_text(cr, "Subida");

    return FALSE;
}

// Actualizar datos cada segundo
gboolean actualizar_monitor(gpointer data) {
    DatosMonitor *dm = (DatosMonitor*)data;
    
    double mem_actual, swap_actual;
    obtener_porcentajes(&mem_actual, &swap_actual);

    unsigned long rx_actual, tx_actual;
    leer_datos_red(&rx_actual, &tx_actual);

    double rx_kb = 0.0;
    double tx_kb = 0.0;
    if (dm->rx_anterior > 0 || dm->tx_anterior > 0) {
        rx_kb = (double)(rx_actual - dm->rx_anterior) / 1024.0;
        tx_kb = (double)(tx_actual - dm->tx_anterior) / 1024.0;
    }
    dm->rx_anterior = rx_actual;
    dm->tx_anterior = tx_actual;
    
    // Guardar nuevos datos
    dm->datos_memoria[dm->posicion_actual] = mem_actual;
    dm->datos_swap[dm->posicion_actual] = swap_actual;
    dm->datos_rx[dm->posicion_actual] = rx_kb;
    dm->datos_tx[dm->posicion_actual] = tx_kb;
    dm->posicion_actual = (dm->posicion_actual + 1) % MAX_PUNTOS;
    
    // Actualizar gráficas
    gtk_widget_queue_draw(dm->drawing_area);
    gtk_widget_queue_draw(dm->drawing_area_red);
    
    return TRUE;
}

// Crear la pestaña de gráficas
GtkWidget *graficas_crear_tab(void)
{
    // Inicializar datos
    static DatosMonitor monitor;
    static int inicializado = 0;
    
    if (!inicializado) {
        monitor.posicion_actual = 0;
        monitor.rx_anterior = 0;
        monitor.tx_anterior = 0;
        for (int i = 0; i < MAX_PUNTOS; i++) {
            monitor.datos_memoria[i] = 0;
            monitor.datos_swap[i] = 0;
            monitor.datos_rx[i] = 0;
            monitor.datos_tx[i] = 0;
        }
        inicializado = 1;
    }
    
    // Contenedor principal
    GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
    gtk_container_set_border_width(GTK_CONTAINER(vbox), 10);
    
    // Título memoria
    GtkWidget *titulo = gtk_label_new(NULL);
    gtk_label_set_markup(GTK_LABEL(titulo), 
        "<span size='large' weight='bold'>Monitor de Memoria y Page File</span>");
    gtk_box_pack_start(GTK_BOX(vbox), titulo, FALSE, FALSE, 5);
    
    // Área de dibujo memoria
    monitor.drawing_area = gtk_drawing_area_new();
    gtk_widget_set_size_request(monitor.drawing_area, 700, 260);
    g_signal_connect(monitor.drawing_area, "draw", G_CALLBACK(dibujar_grafica), &monitor);
    gtk_box_pack_start(GTK_BOX(vbox), monitor.drawing_area, TRUE, TRUE, 5);
    
    // Texto informativo memoria
    GtkWidget *info = gtk_label_new(
        "Gráfica en tiempo real - Actualización cada segundo\n"
        "Azul: Uso de RAM | Rojo: Uso de Page File (Swap)");
    gtk_label_set_justify(GTK_LABEL(info), GTK_JUSTIFY_CENTER);
    gtk_box_pack_start(GTK_BOX(vbox), info, FALSE, FALSE, 5);

    // Separador para la sección de red
    GtkWidget *separator = gtk_separator_new(GTK_ORIENTATION_HORIZONTAL);
    gtk_box_pack_start(GTK_BOX(vbox), separator, FALSE, FALSE, 10);

    // Título red
    GtkWidget *titulo_red = gtk_label_new(NULL);
    gtk_label_set_markup(GTK_LABEL(titulo_red), 
        "<span size='large' weight='bold'>Monitor de Red: Descarga y Subida</span>");
    gtk_box_pack_start(GTK_BOX(vbox), titulo_red, FALSE, FALSE, 5);
    
    // Área de dibujo red
    monitor.drawing_area_red = gtk_drawing_area_new();
    gtk_widget_set_size_request(monitor.drawing_area_red, 700, 260);
    g_signal_connect(monitor.drawing_area_red, "draw", G_CALLBACK(dibujar_grafica_red), &monitor);
    gtk_box_pack_start(GTK_BOX(vbox), monitor.drawing_area_red, TRUE, TRUE, 5);
    
    // Texto informativo red
    GtkWidget *info_red = gtk_label_new(
        "Gráfica en tiempo real - Actualización cada segundo\n"
        "Verde: Descarga | Naranja: Subida");
    gtk_label_set_justify(GTK_LABEL(info_red), GTK_JUSTIFY_CENTER);
    gtk_box_pack_start(GTK_BOX(vbox), info_red, FALSE, FALSE, 5);
    
    // Iniciar actualización periódica
    g_timeout_add_seconds(1, actualizar_monitor, &monitor);
    
    return vbox;
}