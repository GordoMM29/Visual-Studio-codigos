import random
import os

# METODOS DE BUSQUEDA 

# 1. BUSQUEDA SECUENCIAL EN ARREGLO DESORDENADO
def busqueda_secuencial_desordenado(arreglo, objetivo):
    comparaciones = 0
    for i, valor in enumerate(arreglo):
        comparaciones += 1
        if valor == objetivo:
            return i, comparaciones
    return -1, comparaciones

# 2. BUSQUEDA SECUENCIAL EN ARREGLO ORDENADO
def busqueda_secuencial_ordenado(arreglo, objetivo):
    comparaciones = 0
    for i, valor in enumerate(arreglo):
        comparaciones += 1
        if valor == objetivo:
            return i, comparaciones
        if valor > objetivo:
            break
    return -1, comparaciones

# 3. BUSQUEDA BINARIA
def busqueda_binaria(arreglo, objetivo):
    comparaciones = 0
    izquierda, derecha = 0, len(arreglo) - 1
    
    while izquierda <= derecha:
        comparaciones += 1
        medio = (izquierda + derecha) // 2
        
        if arreglo[medio] == objetivo:
            return medio, comparaciones
        elif arreglo[medio] < objetivo:
            izquierda = medio + 1
        else:
            derecha = medio - 1
    
    return -1, comparaciones

# 4. BUSQUEDA INDEXADA
def busqueda_indexada(arreglo, objetivo, indice, paso):
    comparaciones = 0
    
    # Buscar en el indice
    bloque = 0
    while bloque < len(indice) and indice[bloque][1] < objetivo:
        comparaciones += 1
        bloque += 1
    
    # Determinar rango a buscar
    inicio = bloque * paso if bloque > 0 else 0
    fin = min((bloque + 1) * paso, len(arreglo))
    
    # Busqueda secuencial en el bloque
    for i in range(inicio, fin):
        comparaciones += 1
        if arreglo[i] == objetivo:
            return i, comparaciones
    
    return -1, comparaciones

# 5. TABLA HASH
class HashTable:
    def __init__(self, tamanio=100):
        self.tamanio = tamanio
        self.tabla = [None] * tamanio
        self.comparaciones = 0
    
    def funcion_hash(self, clave):
        return clave % 100  # Truncamiento
    
    def insertar(self, valor):
        indice = self.funcion_hash(valor)
        while self.tabla[indice] is not None:
            indice = (indice + 1) % self.tamanio
        self.tabla[indice] = valor
    
    def buscar(self, objetivo):
        self.comparaciones = 0
        indice = self.funcion_hash(objetivo)
        
        while self.tabla[indice] is not None:
            self.comparaciones += 1
            if self.tabla[indice] == objetivo:
                return indice, self.comparaciones
            indice = (indice + 1) % self.tamanio
        
        return -1, self.comparaciones

# INICIALIZACIÓN DE DATOS 

def inicializar_datos():
    # Arreglo desordenado (30 elementos)
    arreglo_desordenado = random.sample(range(1, 100), 30)
    
    # Arreglo ordenado
    arreglo_ordenado = sorted(arreglo_desordenado)
    
    # Arreglo para índice
    arreglo_indexado = sorted(arreglo_desordenado)
    paso = 5
    indice = [(i, arreglo_indexado[i]) for i in range(0, len(arreglo_indexado), paso)]
    
    # Tabla hash con 20 datos de 4 posiciones
    tabla_hash = HashTable(100)
    datos_hash = [1234, 5678, 2345, 6789, 3456, 7890, 4567, 8901, 5673, 9012,
                  6782, 1234, 7891, 2345, 8902, 3456, 9013, 4567, 1237, 5678]
    for valor in datos_hash:
        tabla_hash.insertar(valor)
    
    # Valores a buscar (10 valores fijos)
    valores_buscar = [15, 42, 78, 23, 56, 91, 34, 67, 89, 10]
    
    return arreglo_desordenado, arreglo_ordenado, arreglo_indexado, indice, paso, tabla_hash, valores_buscar

# FUNCIONES DEL MENU

def limpiar_pantalla():
    os.system('cls' if os.name == 'nt' else 'clear')

def mostrar_menu():
    print("=" * 60)
    print("           SISTEMA DE BUSQUEDA DE DATOS")
    print("=" * 60)
    print("1. Buscar un valor especifico")
    print("2. Buscar los 10 valores predefinidos")
    print("3. Mostrar estadisticas de los 10 valores")
    print("4. Ver datos actuales")
    print("5. Salir")
    print("=" * 60)

def buscar_un_valor(datos):
    arreglo_desordenado, arreglo_ordenado, arreglo_indexado, indice, paso, tabla_hash, _ = datos
    
    try:
        valor = int(input("\nIngrese el valor a buscar: "))
        print("\n" + "-" * 60)
        print(f"RESULTADOS PARA EL VALOR {valor}")
        print("-" * 60)
        
        # Secuencial desordenado
        pos, comp = busqueda_secuencial_desordenado(arreglo_desordenado, valor)
        print(f" Secuencial desordenado: Posición={pos}, Comparaciones={comp}")
        
        # Secuencial ordenado
        pos, comp = busqueda_secuencial_ordenado(arreglo_ordenado, valor)
        print(f" Secuencial ordenado:    Posición={pos}, Comparaciones={comp}")
        
        # Binaria
        pos, comp = busqueda_binaria(arreglo_ordenado, valor)
        print(f" Binaria:                Posición={pos}, Comparaciones={comp}")
        
        # Indexada
        pos, comp = busqueda_indexada(arreglo_indexado, valor, indice, paso)
        print(f" Indexada:               Posición={pos}, Comparaciones={comp}")
        
        # Hashing
        pos, comp = tabla_hash.buscar(valor)
        if pos == -1:
            print(f" Hashing:                No encontrado, Comparaciones={comp}")
        else:
            print(f" Hashing:                Posición={pos}, Comparaciones={comp}")
        
        input("\nPresione Enter para continuar...")
    except ValueError:
        print(" Error: Ingrese un número válido")
        input("\nPresione Enter para continuar...")

def buscar_valores_predefinidos(datos):
    arreglo_desordenado, arreglo_ordenado, arreglo_indexado, indice, paso, tabla_hash, valores_buscar = datos
    
    print("\n" + "=" * 80)
    print("BUSCANDO LOS 10 VALORES PREDEFINIDOS")
    print("=" * 80)
    
    for valor in valores_buscar:
        print(f"\n--- Valor: {valor} ---")
        
        pos, comp = busqueda_secuencial_desordenado(arreglo_desordenado, valor)
        print(f"  Secuencial desordenado: {comp} comparaciones", end="")
        if pos != -1:
            print(f" (encontrado en posición {pos})")
        else:
            print(" (no encontrado)")
        
        pos, comp = busqueda_secuencial_ordenado(arreglo_ordenado, valor)
        print(f"  Secuencial ordenado:    {comp} comparaciones", end="")
        if pos != -1:
            print(f" (encontrado en posición {pos})")
        else:
            print(" (no encontrado)")
        
        pos, comp = busqueda_binaria(arreglo_ordenado, valor)
        print(f"  Binaria:                {comp} comparaciones", end="")
        if pos != -1:
            print(f" (encontrado en posición {pos})")
        else:
            print(" (no encontrado)")
        
        pos, comp = busqueda_indexada(arreglo_indexado, valor, indice, paso)
        print(f"  Indexada:               {comp} comparaciones", end="")
        if pos != -1:
            print(f" (encontrado en posición {pos})")
        else:
            print(" (no encontrado)")
        
        pos, comp = tabla_hash.buscar(valor)
        if pos == -1:
            print(f"  Hashing:                {comp} comparaciones (no encontrado)")
        else:
            print(f"  Hashing:                {comp} comparaciones (encontrado en posición {pos})")
    
    input("\nPresione Enter para continuar...")

def mostrar_estadisticas(datos):
    arreglo_desordenado, arreglo_ordenado, arreglo_indexado, indice, paso, tabla_hash, valores_buscar = datos
    
    print("\n" + "=" * 80)
    print("Estadisticas Comparativas (10 valores)")
    print("=" * 80)
    
    total_comp = [0, 0, 0, 0, 0]  # [desordenado, ordenado, binaria, indexada, hash]
    encontrados = [0, 0, 0, 0, 0]
    
    for valor in valores_buscar:
        _, comp = busqueda_secuencial_desordenado(arreglo_desordenado, valor)
        total_comp[0] += comp
        encontrados[0] += 1
        
        _, comp = busqueda_secuencial_ordenado(arreglo_ordenado, valor)
        total_comp[1] += comp
        encontrados[1] += 1
        
        _, comp = busqueda_binaria(arreglo_ordenado, valor)
        total_comp[2] += comp
        encontrados[2] += 1
        
        _, comp = busqueda_indexada(arreglo_indexado, valor, indice, paso)
        total_comp[3] += comp
        encontrados[3] += 1
        
        _, comp = tabla_hash.buscar(valor)
        total_comp[4] += comp
        if tabla_hash.buscar(valor)[0] != -1:
            encontrados[4] += 1
    
    print(f"\n{'Metodo':<25} {'Total':<10} {'Promedio':<10} {'Encontrados':<12}")
    print("-" * 60)
    print(f"{'Secuencial desordenado':<25} {total_comp[0]:<10} {total_comp[0]/10:<10.1f} {encontrados[0]}/10")
    print(f"{'Secuencial ordenado':<25} {total_comp[1]:<10} {total_comp[1]/10:<10.1f} {encontrados[1]}/10")
    print(f"{'Binaria':<25} {total_comp[2]:<10} {total_comp[2]/10:<10.1f} {encontrados[2]}/10")
    print(f"{'Indexada':<25} {total_comp[3]:<10} {total_comp[3]/10:<10.1f} {encontrados[3]}/10")
    print(f"{'Hashing':<25} {total_comp[4]:<10} {total_comp[4]/10:<10.1f} {encontrados[4]}/10")
    
    # Mostrar mejor método
    print("\n" + "-" * 60)
    mejor = min(total_comp)
    mejor_index = total_comp.index(mejor)
    metodos = ["Secuencial desordenado", "Secuencial ordenado", "Binaria", "Indexada", "Hashing"]
    print(f" Mejor Metodo: {metodos[mejor_index]} con {mejor} comparaciones totales")
    
    input("\nPresione Enter para continuar...")

def mostrar_datos(datos):
    arreglo_desordenado, arreglo_ordenado, arreglo_indexado, indice, paso, tabla_hash, valores_buscar = datos
    
    print("\n" + "=" * 80)
    print("DATOS ACTUALES DEL SISTEMA")
    print("=" * 80)
    
    print(f"\n Arreglo desordenado (30 elementos):")
    print(f"   {arreglo_desordenado}")
    
    print(f"\n Arreglo ordenado (30 elementos):")
    print(f"   {arreglo_ordenado}")
    
    print(f"\n Arreglo indexado (30 elementos, paso={paso}):")
    print(f"   {arreglo_indexado}")
    print(f"   Índice: {indice}")
    
    print(f"\n Valores a buscar (10 valores):")
    print(f"   {valores_buscar}")
    
    print(f"\n Tabla Hash (100 posiciones, 20 datos almacenados):")
    datos_almacenados = [x for x in tabla_hash.tabla if x is not None]
    print(f"   Datos: {datos_almacenados}")
    print(f"   Función hash: Truncamiento (módulo 100)")
    
    input("\nPresione Enter para continuar...")

# ========== PROGRAMA PRINCIPAL ==========

def main():
    datos = inicializar_datos()
    
    while True:
        limpiar_pantalla()
        mostrar_menu()
        
        opcion = input("\nSeleccione una opción (1-5): ")
        
        if opcion == '1':
            buscar_un_valor(datos)
        elif opcion == '2':
            buscar_valores_predefinidos(datos)
        elif opcion == '3':
            mostrar_estadisticas(datos)
        elif opcion == '4':
            mostrar_datos(datos)
        elif opcion == '5':
            print("\nGracias por usar el sistema de busqueda :D.")
            print("Saliendo...")
            break
        else:
            print("\nOpcion invalida. Por favor, seleccione 1-5")
            input("\nPresione Enter para continuar...")

if __name__ == "__main__":
    main()