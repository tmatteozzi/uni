import sympy as sp

def signo(valor):
    return "<0" if valor < 0 else ">0"

def biseccion(funcion_str, error_deseado):
    # Definir la variable y la función
    x = sp.symbols('x')
    funcion = sp.sympify(funcion_str)

    # Calcular la raíz exacta de la función dada (Esto es x -> Para calcular luego error |Xm - x|
    raiz_exacta = sp.solve(funcion, x)

    # Tomar solo la primera raíz real si existen múltiples soluciones
    if isinstance(raiz_exacta, list):
        raiz_exacta = raiz_exacta[0]

    # Inicializar variables
    a = 0
    b = 1

    # Buscar el intervalo [a, b]
    print("Buscando intervalo [a, b] donde f(a) * f(b) < 0:")
    f_a = float(funcion.subs(x, a))
    f_b = float(funcion.subs(x, b))

    while f_a * f_b > 0:
        print(f"a = {a}, f(a) = {signo(f_a)}")
        print(f"b = {b}, f(b) = {signo(f_b)}")
        a += 1
        b += 1
        f_a = float(funcion.subs(x, a))
        f_b = float(funcion.subs(x, b))

    # Imprimir el intervalo encontrado
    print(f"a = {a}, f(a) = {signo(f_a)}")
    print(f"b = {b}, f(b) = {signo(f_b)}")
    print(f"Intervalo encontrado: a = {a}, b = {b}")

    # Calcular la cantidad de iteraciones necesarias
    iteraciones = 0
    while (b - a) / (2**iteraciones) >= error_deseado:
        iteraciones += 1
    print(f"Se requieren {iteraciones} iteraciones para un error menor a {error_deseado}")

    # Realizar el método de bisección
    print("\nTabla de iteraciones:")
    encabezado = f"{'n':^5}|{'a':^10}|{'m':^10}|{'b':^10}|{'f(a)':^10}|{'f(m)':^10}|{'f(b)':^10}|{'Error':^20}"
    separador = "-" * len(encabezado)
    print(encabezado)
    print(separador)

    for n in range(iteraciones):
        m = (a + b) / 2
        f_a = float(funcion.subs(x, a))
        f_m = float(funcion.subs(x, m))
        f_b = float(funcion.subs(x, b))

        # Calcular el error como |m - raiz_exacta|
        error_actual = abs(m - float(raiz_exacta))

        fila = (f"{n:^5}|{a:^10.5f}|{m:^10.5f}|{b:^10.5f}|"
                f"{signo(f_a):^10}|{signo(f_m):^10}|"
                f"{signo(f_b):^10}|{error_actual:^20.8f}")
        print(fila)
        print(separador)

        # Actualizar a y b
        if f_a * f_m < 0:
            b = m
        else:
            a = m

    # Resultado final
    m_final = (a + b) / 2
    print(f"\nRaíz aproximada: {m_final:.8f} con un error de {abs(m_final - float(raiz_exacta)):.8f}")

# EJEMPLO DE USO
def main():
    funcion = 'x**3 - 17'
    error = 0.125

    print(f"Función: {funcion}")
    print(f"Error deseado: {error}\n")

    biseccion(funcion, error)

if __name__ == "__main__":
    main()