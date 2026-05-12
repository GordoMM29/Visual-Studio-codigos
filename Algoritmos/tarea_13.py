import tkinter as tk
from tkinter import ttk, scrolledtext, messagebox, simpledialog, filedialog
import random
import time
import sys
import os
import threading
import re
from collections import deque

class ResultadoOrdenamiento:
    def __init__(self, nombre):
        self.nombre_metodo = nombre
        self.tiempo_ejecucion = 0
        self.comparaciones = 0
        self.movimientos = 0
        self.completado = False
        self.error = ""

class OrdenamientoApp:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title("Sistema de Ordenamiento - Python")
        self.root.geometry("700x500")
        self.root.configure(bg='#f0f0f0')
        
        self.arreglo_original = None
        self.tamanio = 0
        self.comparaciones = 0
        self.movimientos = 0
        self.nombre_archivo_actual = None
        self.resultados = []
        
        self.setup_ui()
        
    def setup_ui(self):
        # Frame principal
        main_frame = ttk.Frame(self.root, padding="10")
        main_frame.grid(row=0, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))
        
        # Titulo
        titulo = ttk.Label(main_frame, text="SISTEMA DE ORDENAMIENTO", 
                          font=('Arial', 16, 'bold'))
        titulo.grid(row=0, column=0, columnspan=2, pady=10)
        
        # Subtitulo
        subtitulo = ttk.Label(main_frame, 
            text="ALTA EFICIENCIA: Quick, Merge, Heap, Shell, Radix, Counting, Bin\n"
                 "BAJA EFICIENCIA: Insercion, Seleccion, Burbuja, Arbol, Cocktail",
            font=('Arial', 9), justify='center')
        subtitulo.grid(row=1, column=0, columnspan=2, pady=5)
        
        # Botones
        ttk.Button(main_frame, text="Generar Arreglo Aleatorio", 
                  command=self.generar_arreglo_aleatorio, width=25).grid(row=2, column=0, padx=5, pady=5)
        
        ttk.Button(main_frame, text="Cargar Arreglo desde Archivo", 
                  command=self.cargar_arreglo_desde_archivo, width=25).grid(row=2, column=1, padx=5, pady=5)
        
        ttk.Button(main_frame, text="Ejecutar Metodos Alta Eficiencia", 
                  command=self.ejecutar_metodos_alta_eficiencia, width=30).grid(row=3, column=0, padx=5, pady=5)
        
        ttk.Button(main_frame, text="Ejecutar Metodos Baja Eficiencia", 
                  command=self.ejecutar_metodos_baja_eficiencia, width=30).grid(row=3, column=1, padx=5, pady=5)
        
        ttk.Button(main_frame, text="Salir", 
                  command=self.root.quit, width=25).grid(row=4, column=0, columnspan=2, pady=10)
        
        # Area de texto para logs
        self.log_area = scrolledtext.ScrolledText(main_frame, width=80, height=15, 
                                                   font=('Consolas', 10))
        self.log_area.grid(row=5, column=0, columnspan=2, pady=10, padx=5)
        
        # Barra de progreso
        self.progress = ttk.Progressbar(main_frame, length=600, mode='determinate')
        self.progress.grid(row=6, column=0, columnspan=2, pady=5)
        
        # Estado
        self.estado_label = ttk.Label(main_frame, text="Listo", font=('Arial', 9))
        self.estado_label.grid(row=7, column=0, columnspan=2, pady=5)
        
        # Configurar grid
        self.root.columnconfigure(0, weight=1)
        self.root.rowconfigure(0, weight=1)
        main_frame.columnconfigure(0, weight=1)
        main_frame.columnconfigure(1, weight=1)
        
    def log(self, mensaje):
        """Agregar mensaje al area de logs"""
        self.log_area.insert(tk.END, mensaje + "\n")
        self.log_area.see(tk.END)
        self.root.update_idletasks()
        
    def actualizar_estado(self, mensaje):
        """Actualizar label de estado"""
        self.estado_label.config(text=mensaje)
        self.root.update_idletasks()
        
    def mostrar_primeros(self, arr, n=10):
        """Mostrar primeros n elementos"""
        if not arr:
            return "[]"
        limite = min(n, len(arr))
        primeros = arr[:limite]
        if len(arr) > n:
            return str(primeros)[:-1] + ", ...]"
        return str(primeros)
    
    def mostrar_ultimos(self, arr, n=10):
        """Mostrar ultimos n elementos"""
        if not arr:
            return "[]"
        inicio = max(0, len(arr) - n)
        ultimos = arr[inicio:]
        return str(ultimos)
    
    def encontrar_minimo(self, arr):
        if not arr:
            return None
        return min(arr)
    
    def encontrar_maximo(self, arr):
        if not arr:
            return None
        return max(arr)
    
    def esta_ordenado_descendente(self, arr):
        for i in range(len(arr) - 1):
            if arr[i] < arr[i + 1]:
                return False
        return True
        
    def generar_arreglo_aleatorio(self):
        input_dialog = simpledialog.askstring("Tamano del arreglo", 
            "Ingrese el tamano del arreglo:\n\n" +
            "Recomendaciones:\n" +
            "- Metodos O(n²): maximo 50,000\n" +
            "- Metodos O(n log n): hasta 10,000,000")
        
        if input_dialog is None:
            return
            
        try:
            self.tamanio = int(input_dialog)
            if self.tamanio <= 0:
                messagebox.showerror("Error", "El tamano debe ser mayor a 0")
                return
                
            if self.tamanio > 50_000_000:
                confirm = messagebox.askyesno("Advertencia de memoria",
                    f"Advertencia: {self.tamanio:,} elementos pueden consumir ~" + 
                    f"{(self.tamanio * 4) / 1024 / 1024:.0f} MB de RAM.\n" +
                    "Continuar?")
                if not confirm:
                    return
            
            self.log(f"Generando arreglo aleatorio de {self.tamanio:,} elementos...")
            self.actualizar_estado(f"Generando {self.tamanio:,} elementos...")
            self.progress['maximum'] = self.tamanio
            
            self.arreglo_original = []
            for i in range(self.tamanio):
                self.arreglo_original.append(random.randint(1, self.tamanio * 2))
                if i % max(1, self.tamanio // 100) == 0:
                    self.progress['value'] = i
                    self.root.update_idletasks()
            
            self.progress['value'] = self.tamanio
            self.nombre_archivo_actual = None
            self.log(f"Arreglo aleatorio generado: {self.tamanio:,} elementos")
            self.log(f"  Primeros 10: {self.mostrar_primeros(self.arreglo_original, 10)}")
            self.log(f"  Ultimos 10: {self.mostrar_ultimos(self.arreglo_original, 10)}")
            self.actualizar_estado(f"Listo - {self.tamanio:,} elementos")
            
        except ValueError:
            messagebox.showerror("Error", "Ingrese un numero valido")
        except MemoryError:
            messagebox.showerror("Error de memoria", f"Memoria insuficiente para {self.tamanio} elementos")
    
    def cargar_arreglo_desde_archivo(self):
        archivo = filedialog.askopenfilename(
            title="Seleccionar archivo de numeros",
            filetypes=[("Archivos de texto", "*.txt"), ("Todos los archivos", "*.*")]
        )
        
        if not archivo:
            return
            
        self.nombre_archivo_actual = os.path.basename(archivo)
        
        try:
            with open(archivo, 'r', encoding='utf-8') as f:
                contenido = f.read()
            
            # Filtrar lineas que empiezan con #
            lineas_limpias = []
            for linea in contenido.split('\n'):
                if not linea.strip().startswith('#'):
                    lineas_limpias.append(linea)
            
            contenido_limpio = ' '.join(lineas_limpias)
            numeros_str = re.split(r'[\s,]+', contenido_limpio.strip())
            
            lista_numeros = []
            for num_str in numeros_str:
                if num_str.strip():
                    try:
                        lista_numeros.append(int(num_str.strip()))
                    except ValueError:
                        messagebox.showerror("Error de formato", 
                            f"'{num_str}' no es un numero valido.")
                        return
            
            if not lista_numeros:
                messagebox.showerror("Error", "El archivo esta vacio")
                return
            
            self.tamanio = len(lista_numeros)
            self.arreglo_original = lista_numeros
            
            info = f"Arreglo cargado:\n"
            info += f"Archivo: {self.nombre_archivo_actual}\n"
            info += f"Tamano: {self.tamanio:,} elementos\n"
            info += f"Rango: [{self.encontrar_minimo(self.arreglo_original)}, "
            info += f"{self.encontrar_maximo(self.arreglo_original)}]\n"
            info += f"Primeros 10: {self.mostrar_primeros(self.arreglo_original, 10)}\n"
            info += f"Ultimos 10: {self.mostrar_ultimos(self.arreglo_original, 10)}\n"
            
            if self.esta_ordenado(self.arreglo_original):
                info += "\nEl arreglo ya esta ordenado ascendente."
            elif self.esta_ordenado_descendente(self.arreglo_original):
                info += "\nEl arreglo esta ordenado descendente."
            
            self.log(info)
            self.actualizar_estado(f"Archivo cargado - {self.tamanio:,} elementos")
            messagebox.showinfo("Carga exitosa", info)
            
        except Exception as e:
            messagebox.showerror("Error", f"Error al leer archivo: {str(e)}")
    
    def ejecutar_metodos_alta_eficiencia(self):
        if self.arreglo_original is None:
            messagebox.showwarning("Error", "Primero genere un arreglo")
            return
        
        if self.tamanio > 500000:
            confirm = messagebox.askyesno("Advertencia",
                f"Tamano {self.tamanio:,} es muy grande.\n"
                "Los metodos pueden tardar varios segundos.\n"
                "Continuar?")
            if not confirm:
                return
            
        metodos = [
            "Quick Sort", "Merge Sort", "Heap Sort", "Shell Sort",
            "Radix Sort", "Counting Sort", "Bin Sort"
        ]
        
        threading.Thread(target=self.ejecutar_metodos, args=(metodos, "ALTA EFICIENCIA (Grandes volumenes)"), daemon=True).start()
    
    def ejecutar_metodos_baja_eficiencia(self):
        if self.arreglo_original is None:
            messagebox.showwarning("Error", "Primero genere un arreglo")
            return
        
        if self.tamanio > 50000:
            messagebox.showwarning("Tamano excesivo",
                f"ADVERTENCIA: Metodos de baja eficiencia\n\n"
                f"Tamano actual: {self.tamanio:,} elementos.\n"
                f"Metodos como Burbuja, Insercion y Seleccion pueden tardar\n"
                f"MINUTOS u HORAS con esta cantidad de datos.\n\n"
                f"Se recomienda usar maximo 50,000 elementos.")
            
            confirm = messagebox.askyesno("Confirmacion",
                f"Ejecutar metodos lentos con {self.tamanio:,} elementos?",
                icon='warning')
            if not confirm:
                return
            
        metodos = [
            "Insercion Directa", "Seleccion", "Burbuja", "Arbol Binario", "Cocktail Sort"
        ]
        
        threading.Thread(target=self.ejecutar_metodos, args=(metodos, "BAJA EFICIENCIA (Pocos datos)"), daemon=True).start()
    
    def ejecutar_metodos(self, metodos, tipo_ejecucion):
        resultados = []
        
        self.log("\n" + "="*70)
        self.log(f"INICIANDO EJECUCION - {tipo_ejecucion}")
        self.log("="*70)
        
        self.progress['maximum'] = len(metodos)
        self.progress['value'] = 0
        self.actualizar_estado(f"Ejecutando {len(metodos)} metodos...")
        
        for i, metodo in enumerate(metodos):
            self.progress['value'] = i
            self.actualizar_estado(f"Ejecutando {metodo} ({i+1}/{len(metodos)})...")
            self.log(f"Ejecutando: {metodo} ({i+1}/{len(metodos)})...")
            self.root.update_idletasks()
            
            try:
                copia = self.arreglo_original.copy()
                self.comparaciones = 0
                self.movimientos = 0
                
                inicio = time.time()
                
                if metodo == "Insercion Directa":
                    self.insercion_directa(copia)
                elif metodo == "Seleccion":
                    self.seleccion(copia)
                elif metodo == "Burbuja":
                    self.burbuja(copia)
                elif metodo == "Quick Sort":
                    self.quick_sort(copia, 0, len(copia)-1)
                elif metodo == "Merge Sort":
                    self.merge_sort(copia, 0, len(copia)-1)
                elif metodo == "Heap Sort":
                    self.heap_sort(copia)
                elif metodo == "Shell Sort":
                    self.shell_sort(copia)
                elif metodo == "Radix Sort":
                    self.radix_sort(copia)
                elif metodo == "Counting Sort":
                    self.counting_sort(copia)
                elif metodo == "Bin Sort":
                    self.bin_sort(copia)
                elif metodo == "Arbol Binario":
                    self.tree_sort(copia)
                elif metodo == "Cocktail Sort":
                    self.cocktail_sort(copia)
                    
                fin = time.time()
                tiempo = (fin - inicio) * 1000
                
                ordenado = self.esta_ordenado(copia)
                
                resultado = ResultadoOrdenamiento(metodo)
                resultado.tiempo_ejecucion = tiempo
                resultado.comparaciones = self.comparaciones
                resultado.movimientos = self.movimientos
                resultado.completado = ordenado
                resultados.append(resultado)
                
                estado = "OK" if ordenado else "ERROR"
                self.log(f"  {estado}: {tiempo:.2f} ms | {self.comparaciones:,} comp | {self.movimientos:,} mov")
                
            except RecursionError:
                self.log(f"  Error: Stack Overflow")
                resultado = ResultadoOrdenamiento(metodo)
                resultado.completado = False
                resultado.error = "Stack Overflow"
                resultados.append(resultado)
            except MemoryError:
                self.log(f"  Error: Memoria insuficiente")
                resultado = ResultadoOrdenamiento(metodo)
                resultado.completado = False
                resultado.error = "Out of Memory"
                resultados.append(resultado)
            except Exception as e:
                self.log(f"  Error: {str(e)}")
                resultado = ResultadoOrdenamiento(metodo)
                resultado.completado = False
                resultado.error = str(e)[:50]
                resultados.append(resultado)
                
            self.root.update_idletasks()
        
        self.progress['value'] = len(metodos)
        self.actualizar_estado("Ejecucion completada!")
        self.log("Todos los metodos han sido ejecutados")
        self.log("Generando ventana de resultados...")
        
        self.root.after(100, lambda: self.mostrar_resultados(resultados, tipo_ejecucion))
        
    def mostrar_resultados(self, resultados, tipo_ejecucion):
        try:
            result_window = tk.Toplevel(self.root)
            result_window.title(f"Resultados - {tipo_ejecucion}")
            result_window.geometry("1100x650")
            result_window.configure(bg='#f0f0f0')
            
            frame = ttk.Frame(result_window, padding="10")
            frame.pack(fill=tk.BOTH, expand=True)
            
            titulo_frame = ttk.LabelFrame(frame, text=f"TIPO: {tipo_ejecucion}", padding="5")
            titulo_frame.pack(fill=tk.X, pady=(0, 10))
            
            tree_frame = ttk.Frame(frame)
            tree_frame.pack(fill=tk.BOTH, expand=True)
            
            columns = ("Metodo", "Tiempo (ms)", "Comparaciones", "Movimientos", "Estado")
            tree = ttk.Treeview(tree_frame, columns=columns, show="headings", height=15)
            
            tree.heading("Metodo", text="METODO")
            tree.heading("Tiempo (ms)", text="Tiempo (ms)")
            tree.heading("Comparaciones", text="Comparaciones")
            tree.heading("Movimientos", text="Movimientos")
            tree.heading("Estado", text="Estado")
            
            tree.column("Metodo", width=200)
            tree.column("Tiempo (ms)", width=120)
            tree.column("Comparaciones", width=150)
            tree.column("Movimientos", width=150)
            tree.column("Estado", width=100)
            
            scrollbar_y = ttk.Scrollbar(tree_frame, orient=tk.VERTICAL, command=tree.yview)
            scrollbar_x = ttk.Scrollbar(tree_frame, orient=tk.HORIZONTAL, command=tree.xview)
            tree.configure(yscrollcommand=scrollbar_y.set, xscrollcommand=scrollbar_x.set)
            
            tree.grid(row=0, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))
            scrollbar_y.grid(row=0, column=1, sticky=(tk.N, tk.S))
            scrollbar_x.grid(row=1, column=0, sticky=(tk.W, tk.E))
            
            tree_frame.columnconfigure(0, weight=1)
            tree_frame.rowconfigure(0, weight=1)
            
            for r in resultados:
                if r.completado:
                    estado = "OK"
                else:
                    estado = f"ERROR {r.error[:10] if r.error else ''}"
                tree.insert("", tk.END, values=(
                    r.nombre_metodo,
                    f"{r.tiempo_ejecucion:.2f}",
                    f"{r.comparaciones:,}",
                    f"{r.movimientos:,}",
                    estado
                ))
            
            info_frame = ttk.LabelFrame(frame, text="Informacion del Sistema", padding="10")
            info_frame.pack(fill=tk.X, pady=10)
            
            fuente = "Archivo" if self.nombre_archivo_actual else "Aleatorio"
            nombre_archivo_info = f" ({self.nombre_archivo_actual})" if self.nombre_archivo_actual else ""
            
            info_text = f"""
Fuente: {fuente}{nombre_archivo_info}
Tamano: {self.tamanio:,} elementos
Memoria: {sys.maxsize // (1024**2):,} MB
Procesadores: {os.cpu_count() if hasattr(os, 'cpu_count') else 'N/A'}
Sistema: {sys.platform}
Python: {sys.version.split()[0]}
            """
            
            info_label = ttk.Label(info_frame, text=info_text, font=('Consolas', 10))
            info_label.pack(anchor=tk.W)
            
            btn_frame = ttk.Frame(frame)
            btn_frame.pack(pady=10)
            ttk.Button(btn_frame, text="Cerrar", command=result_window.destroy).pack()
            
            result_window.lift()
            result_window.focus_force()
            
            self.log("Ventana de resultados mostrada")
            
        except Exception as e:
            self.log(f"Error al mostrar resultados: {str(e)}")
            messagebox.showerror("Error", f"No se pudieron mostrar resultados: {str(e)}")
    
    # ==================== METODOS DE ORDENAMIENTO ====================
    
    def insercion_directa(self, arr):
        for i in range(1, len(arr)):
            aux = arr[i]
            j = i - 1
            while j >= 0 and aux < arr[j]:
                self.comparaciones += 1
                arr[j + 1] = arr[j]
                self.movimientos += 1
                j -= 1
            self.comparaciones += 1
            arr[j + 1] = aux
            self.movimientos += 1
            
    def seleccion(self, arr):
        for i in range(len(arr) - 1):
            min_idx = i
            for j in range(i + 1, len(arr)):
                self.comparaciones += 1
                if arr[j] < arr[min_idx]:
                    min_idx = j
            if min_idx != i:
                arr[i], arr[min_idx] = arr[min_idx], arr[i]
                self.movimientos += 1
                
    def burbuja(self, arr):
        n = len(arr)
        for i in range(n - 1):
            swapped = False
            for j in range(n - 1 - i):
                self.comparaciones += 1
                if arr[j] > arr[j + 1]:
                    arr[j], arr[j + 1] = arr[j + 1], arr[j]
                    self.movimientos += 1
                    swapped = True
            if not swapped:
                break
                
    def quick_sort(self, arr, low, high):
        if low < high:
            pi = self.particion(arr, low, high)
            self.quick_sort(arr, low, pi - 1)
            self.quick_sort(arr, pi + 1, high)
            
    def particion(self, arr, low, high):
        pivot = arr[high]
        i = low - 1
        for j in range(low, high):
            self.comparaciones += 1
            if arr[j] <= pivot:
                i += 1
                arr[i], arr[j] = arr[j], arr[i]
                self.movimientos += 1
        arr[i + 1], arr[high] = arr[high], arr[i + 1]
        self.movimientos += 1
        return i + 1
        
    def merge_sort(self, arr, left, right):
        if left < right:
            mid = (left + right) // 2
            self.merge_sort(arr, left, mid)
            self.merge_sort(arr, mid + 1, right)
            self.merge(arr, left, mid, right)
            
    def merge(self, arr, left, mid, right):
        n1 = mid - left + 1
        n2 = right - mid
        
        L = arr[left:left + n1]
        R = arr[mid + 1:mid + 1 + n2]
        self.movimientos += n1 + n2
        
        i = j = 0
        k = left
        
        while i < n1 and j < n2:
            self.comparaciones += 1
            if L[i] <= R[j]:
                arr[k] = L[i]
                i += 1
            else:
                arr[k] = R[j]
                j += 1
            self.movimientos += 1
            k += 1
            
        while i < n1:
            arr[k] = L[i]
            i += 1
            k += 1
            self.movimientos += 1
            
        while j < n2:
            arr[k] = R[j]
            j += 1
            k += 1
            self.movimientos += 1
            
    def heap_sort(self, arr):
        n = len(arr)
        
        for i in range(n // 2 - 1, -1, -1):
            self.heapify(arr, n, i)
            
        for i in range(n - 1, 0, -1):
            arr[0], arr[i] = arr[i], arr[0]
            self.movimientos += 1
            self.heapify(arr, i, 0)
            
    def heapify(self, arr, n, i):
        largest = i
        left = 2 * i + 1
        right = 2 * i + 2
        
        self.comparaciones += 1
        if left < n and arr[left] > arr[largest]:
            largest = left
            
        self.comparaciones += 1
        if right < n and arr[right] > arr[largest]:
            largest = right
            
        if largest != i:
            arr[i], arr[largest] = arr[largest], arr[i]
            self.movimientos += 1
            self.heapify(arr, n, largest)
            
    def shell_sort(self, arr):
        n = len(arr)
        gap = n // 2
        while gap > 0:
            for i in range(gap, n):
                temp = arr[i]
                j = i
                while j >= gap and arr[j - gap] > temp:
                    self.comparaciones += 1
                    arr[j] = arr[j - gap]
                    self.movimientos += 1
                    j -= gap
                if j >= gap:
                    self.comparaciones += 1
                arr[j] = temp
                self.movimientos += 1
            gap //= 2
            
    def radix_sort(self, arr):
        if not arr:
            return
            
        max_val = max(arr)
        exp = 1
        while max_val // exp > 0:
            self.counting_sort_radix(arr, exp)
            exp *= 10
            
    def counting_sort_radix(self, arr, exp):
        n = len(arr)
        output = [0] * n
        count = [0] * 10
        
        for i in range(n):
            idx = (arr[i] // exp) % 10
            count[idx] += 1
            self.movimientos += 1
            
        for i in range(1, 10):
            count[i] += count[i - 1]
            
        for i in range(n - 1, -1, -1):
            idx = (arr[i] // exp) % 10
            output[count[idx] - 1] = arr[i]
            count[idx] -= 1
            self.movimientos += 1
            
        for i in range(n):
            arr[i] = output[i]
            self.movimientos += 1
            
    def counting_sort(self, arr):
        if not arr:
            return
            
        max_val = max(arr)
        min_val = min(arr)
        range_val = max_val - min_val + 1
        
        if range_val > 10_000_000:
            raise Exception(f"Rango demasiado grande: {range_val}")
            
        count = [0] * range_val
        output = [0] * len(arr)
        
        for num in arr:
            count[num - min_val] += 1
            self.movimientos += 1
            
        for i in range(1, range_val):
            count[i] += count[i - 1]
            
        for i in range(len(arr) - 1, -1, -1):
            output[count[arr[i] - min_val] - 1] = arr[i]
            count[arr[i] - min_val] -= 1
            self.movimientos += 1
            
        for i in range(len(arr)):
            arr[i] = output[i]
            self.movimientos += 1
            
    def bin_sort(self, arr):
        if not arr:
            return
            
        max_val = max(arr)
        min_val = min(arr)
        
        if max_val == min_val:
            return
            
        num_buckets = max(1, int(len(arr) ** 0.5))
        buckets = [[] for _ in range(num_buckets)]
        
        for num in arr:
            idx = int((num - min_val) * (num_buckets - 1) / (max_val - min_val))
            idx = min(idx, num_buckets - 1)
            buckets[idx].append(num)
            self.movimientos += 1
            
        index = 0
        for bucket in buckets:
            bucket.sort()
            for val in bucket:
                arr[index] = val
                index += 1
                self.movimientos += 1
                
    class NodoArbol:
        def __init__(self, valor):
            self.valor = valor
            self.izquierdo = None
            self.derecho = None
            
    def tree_sort(self, arr):
        if not arr:
            return
            
        raiz = self.NodoArbol(arr[0])
        self.movimientos += 1
        
        for i in range(1, len(arr)):
            self.insertar_nodo(raiz, arr[i])
            
        lista = []
        self.recorrido_inorden(raiz, lista)
        
        for i in range(len(lista)):
            arr[i] = lista[i]
            self.movimientos += 1
            
    def insertar_nodo(self, nodo, valor):
        self.comparaciones += 1
        if valor < nodo.valor:
            if nodo.izquierdo is None:
                nodo.izquierdo = self.NodoArbol(valor)
                self.movimientos += 1
            else:
                self.insertar_nodo(nodo.izquierdo, valor)
        else:
            if nodo.derecho is None:
                nodo.derecho = self.NodoArbol(valor)
                self.movimientos += 1
            else:
                self.insertar_nodo(nodo.derecho, valor)
                
    def recorrido_inorden(self, nodo, lista):
        if nodo:
            self.recorrido_inorden(nodo.izquierdo, lista)
            lista.append(nodo.valor)
            self.recorrido_inorden(nodo.derecho, lista)
            
    def cocktail_sort(self, arr):
        n = len(arr)
        swapped = True
        start = 0
        end = n - 1
        
        while swapped:
            swapped = False
            for i in range(start, end):
                self.comparaciones += 1
                if arr[i] > arr[i + 1]:
                    arr[i], arr[i + 1] = arr[i + 1], arr[i]
                    self.movimientos += 1
                    swapped = True
                    
            if not swapped:
                break
                
            swapped = False
            end -= 1
            
            for i in range(end - 1, start - 1, -1):
                self.comparaciones += 1
                if arr[i] > arr[i + 1]:
                    arr[i], arr[i + 1] = arr[i + 1], arr[i]
                    self.movimientos += 1
                    swapped = True
                    
            start += 1
            
    def esta_ordenado(self, arr):
        for i in range(len(arr) - 1):
            if arr[i] > arr[i + 1]:
                return False
        return True
        
    def run(self):
        self.root.mainloop()

if __name__ == "__main__":
    app = OrdenamientoApp()
    app.run()