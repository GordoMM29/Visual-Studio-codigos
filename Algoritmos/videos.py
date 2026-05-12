import matplotlib.pyplot as plt
import matplotlib.animation as animation
import matplotlib.patches as patches
from matplotlib.patches import Circle, Rectangle, FancyBboxPatch
import numpy as np
import time
from PIL import Image
import io

class VisualizadorPancakes:
    def __init__(self, datos):
        self.datos_original = datos.copy()
        self.datos = datos.copy()
        self.n = len(datos)
        self.fig, self.ax = plt.subplots(figsize=(14, 8))
        self.pancakes = []
        self.pasos = []
        
        # Colores de pancakes (sabores)
        self.colores_pancakes = [
            '#F4A460',  # Panqueque dorado
            '#DEB887',  # Panqueque claro
            '#D2691E',  # Panqueque chocolate
            '#FFDAB9',  # Panqueque vainilla
            '#CD853F',  # Panqueque miel
            '#F5DEB3',  # Panqueque trigo
            '#E8B06D',  # Panqueque maple
            '#C4A47A'   # Panqueque integral
        ]
        
    def registrar_paso(self, titulo=""):
        """Registra el estado actual"""
        self.pasos.append((self.datos.copy(), titulo))
    
    def dibujar_pancake(self, x, y, tamaño, valor, color):
        """Dibuja un pancake individual"""
        # Base del panqueque (elipse)
        pancake = patches.Ellipse((x, y), tamaño * 0.8, tamaño * 0.3, 
                                    facecolor=color, edgecolor='#8B4513', linewidth=2)
        self.ax.add_patch(pancake)
        
        # Parte superior (un poco más pequeña para dar efecto 3D)
        pancake_top = patches.Ellipse((x, y + tamaño * 0.08), tamaño * 0.7, tamaño * 0.25,
                                       facecolor=color, edgecolor='#8B4513', linewidth=1.5)
        self.ax.add_patch(pancake_top)
        
        # Jarabe (mancha de maple)
        jarabe = patches.Ellipse((x - tamaño*0.1, y + tamaño*0.05), tamaño*0.2, tamaño*0.08,
                                  facecolor='#8B4513', alpha=0.6)
        self.ax.add_patch(jarabe)
        
        # Mantequilla (pequeño rectángulo)
        mantequilla = patches.Rectangle((x - tamaño*0.12, y + tamaño*0.1), 
                                         tamaño*0.1, tamaño*0.05,
                                         facecolor='#FFD700', edgecolor='#DAA520', 
                                         linewidth=1, angle=15)
        self.ax.add_patch(mantequilla)
        
        # Número (valor) en el centro
        self.ax.text(x, y, str(valor), ha='center', va='center', 
                    fontsize=12, fontweight='bold', color='white',
                    bbox=dict(boxstyle='round,pad=0.3', facecolor='#8B4513', alpha=0.8))
        
        return pancake
    
    def dibujar_pila_pancakes(self):
        """Dibuja todas las pilas de pancakes"""
        self.ax.clear()
        
        # Configurar el gráfico
        self.ax.set_xlim(-1, self.n)
        self.ax.set_ylim(0, max(self.datos) * 1.2 + 2)
        self.ax.set_facecolor('#FFF8DC')  # Color crema (mesa)
        
        # Título y etiquetas
        self.ax.set_title(f"🥞 ORDENAMIENTO DE PANCAKES 🥞\n{self.titulo_actual}", 
                         fontsize=14, fontweight='bold', color='#8B4513')
        self.ax.set_xlabel("Posición de la pila", fontsize=12, fontweight='bold')
        self.ax.set_ylabel("Altura del pancake", fontsize=12, fontweight='bold')
        
        # Dibujar platos base
        for i in range(self.n):
            plato = patches.Ellipse((i, 0.3), 0.7, 0.2, 
                                     facecolor='#E8E8E8', edgecolor='#A0A0A0', linewidth=2)
            self.ax.add_patch(plato)
        
        # Dibujar pancakes apilados
        for i, altura in enumerate(self.datos):
            # Apilar pancakes desde la base
            for j in range(altura):
                y_base = 0.5 + j * 0.35  # Altura de cada pancake
                color = self.colores_pancakes[j % len(self.colores_pancakes)]
                
                pancake = patches.FancyBboxPatch(
                    (i - 0.35, y_base), 0.7, 0.3,
                    boxstyle=patches.BoxStyle("round", pad=0.05),
                    facecolor=color, edgecolor='#8B4513', linewidth=2
                )
                self.ax.add_patch(pancake)
                
                # Detalles decorativos
                if j == altura - 1:  # Pancake superior
                    # Jarabe en el de arriba
                    jarabe = patches.Ellipse((i, y_base + 0.2), 0.4, 0.08,
                                              facecolor='#8B4513', alpha=0.5)
                    self.ax.add_patch(jarabe)
            
            # Etiqueta del valor
            self.ax.text(i, 0.2, str(altura), ha='center', va='center', 
                        fontsize=10, fontweight='bold', color='#8B4513')
            
            # Número de posición
            self.ax.text(i, -0.3, f"[{i}]", ha='center', va='center', 
                        fontsize=9, color='#666')
        
        # Decoración de la mesa
        self.ax.axhline(y=0, color='#8B4513', linewidth=3, alpha=0.3)
        
        # Eliminar spines superiores y derechos
        self.ax.spines['top'].set_visible(False)
        self.ax.spines['right'].set_visible(False)
        self.ax.spines['left'].set_color('#8B4513')
        self.ax.spines['bottom'].set_color('#8B4513')
        
        self.ax.grid(True, alpha=0.2, linestyle='--')
        
    def animar(self, intervalo=1200):
        """Anima la secuencia"""
        def update(frame):
            datos, titulo = self.pasos[frame]
            self.datos = datos
            self.titulo_actual = titulo
            self.dibujar_pila_pancakes()
            return []
        
        self.titulo_actual = self.pasos[0][1] if self.pasos else "Inicio"
        self.dibujar_pila_pancakes()
        
        self.animacion = animation.FuncAnimation(
            self.fig, update, frames=len(self.pasos), 
            interval=intervalo, repeat=False, blit=False
        )
        plt.tight_layout()
        plt.show()
    
    # ==================== MÉTODOS DE ORDENAMIENTO ====================
    
    def insercion_directa(self):
        self.datos = self.datos_original.copy()
        self.pasos = []
        self.registrar_paso(f"🥞 INSERCIÓN - Comenzamos con {self.datos}")
        
        for i in range(1, self.n):
            aux = self.datos[i]
            j = i - 1
            while j >= 0 and aux < self.datos[j]:
                self.datos[j + 1] = self.datos[j]
                self.registrar_paso(f"🥞 Deslizando pancake {self.datos[j]} → posición {j+1}")
                j -= 1
            self.datos[j + 1] = aux
            self.registrar_paso(f"🥞 Insertando pancake {aux} en posición {j+1} → {self.datos}")
        
        self.registrar_paso(f"✅ INSERCIÓN COMPLETADA → {self.datos}")
        self.animar()
    
    def seleccion(self):
        self.datos = self.datos_original.copy()
        self.pasos = []
        self.registrar_paso(f"🥞 SELECCIÓN - Buscando el pancake más pequeño")
        
        for i in range(self.n - 1):
            min_idx = i
            for j in range(i + 1, self.n):
                if self.datos[j] < self.datos[min_idx]:
                    min_idx = j
                    self.registrar_paso(f"🔍 Nuevo pancake más pequeño: {self.datos[min_idx]}")
            
            if min_idx != i:
                self.datos[i], self.datos[min_idx] = self.datos[min_idx], self.datos[i]
                self.registrar_paso(f"🔄 Intercambiando pancake {self.datos[i]} con {self.datos[min_idx]} → {self.datos}")
        
        self.registrar_paso(f"✅ SELECCIÓN COMPLETADA → {self.datos}")
        self.animar()
    
    def burbuja(self):
        self.datos = self.datos_original.copy()
        self.pasos = []
        self.registrar_paso(f"🥞 BURBUJA - Los pancakes grandes suben como burbujas")
        
        for i in range(self.n - 1):
            for j in range(self.n - 1 - i):
                if self.datos[j] > self.datos[j + 1]:
                    self.datos[j], self.datos[j + 1] = self.datos[j + 1], self.datos[j]
                    self.registrar_paso(f"💨 Pancake {self.datos[j+1]} burbujea hacia arriba → {self.datos}")
        
        self.registrar_paso(f"✅ BURBUJA COMPLETADA → {self.datos}")
        self.animar()
    
    def quick_sort(self, low=0, high=None):
        if high is None:
            high = self.n - 1
            self.datos = self.datos_original.copy()
            self.pasos = []
            self.registrar_paso(f"🥞 QUICK SORT - Elegimos un pancake pivote")
            self._quick_sort(low, high)
            self.registrar_paso(f"✅ QUICK SORT COMPLETADO → {self.datos}")
            self.animar()
    
    def _quick_sort(self, low, high):
        if low < high:
            pi = self._particion(low, high)
            self._quick_sort(low, pi - 1)
            self._quick_sort(pi + 1, high)
    
    def _particion(self, low, high):
        pivot = self.datos[high]
        i = low - 1
        
        for j in range(low, high):
            if self.datos[j] <= pivot:
                i += 1
                self.datos[i], self.datos[j] = self.datos[j], self.datos[i]
                self.registrar_paso(f"🎯 Pancake pivote = {pivot}, reorganizando → {self.datos}")
        
        self.datos[i + 1], self.datos[high] = self.datos[high], self.datos[i + 1]
        self.registrar_paso(f"📍 Pivote {pivot} en su posición correcta → {self.datos}")
        return i + 1
    
    def merge_sort(self, left=0, right=None):
        if right is None:
            right = self.n - 1
            self.datos = self.datos_original.copy()
            self.pasos = []
            self.registrar_paso(f"🥞 MERGE SORT - Dividiendo la pila de pancakes")
            self._merge_sort(left, right)
            self.registrar_paso(f"✅ MERGE SORT COMPLETADO → {self.datos}")
            self.animar()
    
    def _merge_sort(self, left, right):
        if left < right:
            mid = (left + right) // 2
            self._merge_sort(left, mid)
            self._merge_sort(mid + 1, right)
            self._merge(left, mid, right)
    
    def _merge(self, left, mid, right):
        n1 = mid - left + 1
        n2 = right - mid
        
        L = self.datos[left:left + n1]
        R = self.datos[mid + 1:mid + 1 + n2]
        
        i = j = 0
        k = left
        
        while i < n1 and j < n2:
            if L[i] <= R[j]:
                self.datos[k] = L[i]
                i += 1
            else:
                self.datos[k] = R[j]
                j += 1
            k += 1
            self.registrar_paso(f"🔀 Fusionando pancakes → {self.datos}")
        
        while i < n1:
            self.datos[k] = L[i]
            i += 1
            k += 1
            self.registrar_paso(f"🔀 Fusionando pancakes → {self.datos}")
        
        while j < n2:
            self.datos[k] = R[j]
            j += 1
            k += 1
            self.registrar_paso(f"🔀 Fusionando pancakes → {self.datos}")
    
    def cocktail_sort(self):
        self.datos = self.datos_original.copy()
        self.pasos = []
        self.registrar_paso(f"🥞 COCKTAIL - Pancakes van y vienen como coctelera")
        
        swapped = True
        start = 0
        end = self.n - 1
        
        while swapped:
            swapped = False
            for i in range(start, end):
                if self.datos[i] > self.datos[i + 1]:
                    self.datos[i], self.datos[i + 1] = self.datos[i + 1], self.datos[i]
                    swapped = True
                    self.registrar_paso(f"🔄 Ida: Intercambiando → {self.datos}")
            
            if not swapped:
                break
            
            swapped = False
            end -= 1
            
            for i in range(end - 1, start - 1, -1):
                if self.datos[i] > self.datos[i + 1]:
                    self.datos[i], self.datos[i + 1] = self.datos[i + 1], self.datos[i]
                    swapped = True
                    self.registrar_paso(f"🔄 Vuelta: Intercambiando → {self.datos}")
            
            start += 1
        
        self.registrar_paso(f"✅ COCKTAIL COMPLETADO → {self.datos}")
        self.animar()
    
    def heap_sort(self):
        self.datos = self.datos_original.copy()
        self.pasos = []
        self.registrar_paso(f"🥞 HEAP SORT - Construyendo montaña de pancakes")
        
        n = self.n
        for i in range(n // 2 - 1, -1, -1):
            self._heapify(n, i)
        
        for i in range(n - 1, 0, -1):
            self.datos[0], self.datos[i] = self.datos[i], self.datos[0]
            self.registrar_paso(f"⛰️ Extrayendo pancake más grande → {self.datos}")
            self._heapify(i, 0)
        
        self.registrar_paso(f"✅ HEAP SORT COMPLETADO → {self.datos}")
        self.animar()
    
    def _heapify(self, n, i):
        largest = i
        left = 2 * i + 1
        right = 2 * i + 2
        
        if left < n and self.datos[left] > self.datos[largest]:
            largest = left
        if right < n and self.datos[right] > self.datos[largest]:
            largest = right
        
        if largest != i:
            self.datos[i], self.datos[largest] = self.datos[largest], self.datos[i]
            self.registrar_paso(f"🏔️ Reordenando montaña → {self.datos}")
            self._heapify(n, largest)
    
    def shell_sort(self):
        self.datos = self.datos_original.copy()
        self.pasos = []
        self.registrar_paso(f"🥞 SHELL SORT - Agrupando pancakes por distancia")
        
        n = self.n
        gap = n // 2
        while gap > 0:
            for i in range(gap, n):
                temp = self.datos[i]
                j = i
                while j >= gap and self.datos[j - gap] > temp:
                    self.datos[j] = self.datos[j - gap]
                    j -= gap
                    self.registrar_paso(f"📏 Gap={gap}: Reordenando → {self.datos}")
                self.datos[j] = temp
                self.registrar_paso(f"📏 Gap={gap}: Insertando → {self.datos}")
            gap //= 2
        
        self.registrar_paso(f"✅ SHELL SORT COMPLETADO → {self.datos}")
        self.animar()


# ==================== PROGRAMA PRINCIPAL ====================

def main():
    # Pancakes con diferentes tamaños (valores)
    DATOS = [7, 3, 8, 2, 5, 1, 6, 4]  # 8 pancakes de diferentes tamaños
    
    print("=" * 70)
    print("🥞 VISUALIZADOR DE ORDENAMIENTO DE PANCAKES 🥞")
    print("=" * 70)
    print(f"\nPancakes iniciales (tamaños): {DATOS}")
    print("\n📚 Métodos disponibles:")
    print("1. 🥞 Inserción Directa - Como ordenar pancakes uno por uno")
    print("2. 🎯 Selección - Buscar el pancake más pequeño")
    print("3. 💨 Burbuja - Los pancakes grandes suben")
    print("4. 🎯 Quick Sort - Dividir y conquistar")
    print("5. 🔀 Merge Sort - Unir pilas ordenadas")
    print("6. 🥤 Cocktail Sort - Va y viene como coctelera")
    print("7. ⛰️ Heap Sort - Construir montaña de pancakes")
    print("8. 📏 Shell Sort - Agrupar por distancia")
    print("0. 🎬 Ejecutar TODOS (recomendado para video)")
    
    opcion = input("\n🍽️ Elige un método (0-8): ").strip()
    
    vis = VisualizadorPancakes(DATOS)
    
    if opcion == "1":
        print("\n🥞 Ordenando con Inserción Directa...")
        vis.insercion_directa()
    elif opcion == "2":
        print("\n🎯 Ordenando con Selección...")
        vis.seleccion()
    elif opcion == "3":
        print("\n💨 Ordenando con Burbuja...")
        vis.burbuja()
    elif opcion == "4":
        print("\n🎯 Ordenando con Quick Sort...")
        vis.quick_sort()
    elif opcion == "5":
        print("\n🔀 Ordenando con Merge Sort...")
        vis.merge_sort()
    elif opcion == "6":
        print("\n🥤 Ordenando con Cocktail Sort...")
        vis.cocktail_sort()
    elif opcion == "7":
        print("\n⛰️ Ordenando con Heap Sort...")
        vis.heap_sort()
    elif opcion == "8":
        print("\n📏 Ordenando con Shell Sort...")
        vis.shell_sort()
    elif opcion == "0":
        print("\n🎬 Ejecutando TODOS los métodos para grabar video...")
        
        metodos = [
            ("Inserción Directa", vis.insercion_directa),
            ("Selección", vis.seleccion),
            ("Burbuja", vis.burbuja),
            ("Quick Sort", vis.quick_sort),
            ("Merge Sort", vis.merge_sort),
            ("Cocktail Sort", vis.cocktail_sort),
            ("Heap Sort", vis.heap_sort),
            ("Shell Sort", vis.shell_sort),
        ]
        
        for nombre, metodo in metodos:
            print(f"\n▶ {nombre}")
            input("Presiona ENTER para comenzar...")
            nuevo_vis = VisualizadorPancakes(DATOS)
            metodo(nuevo_vis)
            time.sleep(1)
    else:
        print("Opción no válida")


if __name__ == "__main__":
    try:
        import matplotlib
        print(f"🥞 Matplotlib versión: {matplotlib.__version__}")
        print("🎬 Iniciando visualizador de pancakes...")
    except ImportError:
        print("Error: Necesitas instalar matplotlib")
        print("Ejecuta: pip install matplotlib")
        exit(1)
    
    main()