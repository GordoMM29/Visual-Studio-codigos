def factorial(n):
    if n <= 1:
        return 1
    return n * factorial(n - 1)

def fibonacci(n):
    if n <= 1:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

while True:
    opcion = input("1. Factorial\n2. Fibonacci\n3. Salir\n\nSeleccione opción: ")
    
    if opcion == "3":
        break
    
    if opcion == "1":
        try:
            num = int(input("Ingrese número para factorial: "))
            print(f"Factorial de {num}: {factorial(num)}")
        except:
            print("Error en los datos")
    
    elif opcion == "2":
        try:
            num = int(input("Ingrese posición para fibonacci: "))
            print(f"Fibonacci en posición {num}: {fibonacci(num)}")
        except:
            print("Error en los datos")