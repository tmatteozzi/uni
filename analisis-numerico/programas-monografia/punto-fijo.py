import numpy as np
import sympy as sp

def punto_fijo(g_func, intervalo, error):
    # Definir variable simbólica
    x = sp.symbols('x')

    # Derivar la función g(x) simbólicamente
    g = g_func(x)
    g_prime = sp.diff(g, x)

    a, b = intervalo

    # Validar la primera condición: g(x) pertenece a [a, b] para x en [a, b]
    print("\nValidando la primera condición:")
    for i in np.linspace(a, b, 100):
        g_value = g_func(i)
        if g_value < a or g_value > b:
            print(f"La función g(x) no está dentro del intervalo en x = {i}")
            return
    print("La primera condición se cumple: g(x) pertenece a [a, b]")

    # Validar la segunda condición: |g'(x)| < 1 en todo [a, b] y encontrar L
    print("\nValidando la segunda condición:")
    g_prime_func = sp.lambdify(x, g_prime)
    L = 0
    for i in np.linspace(a, b, 100):
        g_prime_value = abs(g_prime_func(i))
        if g_prime_value >= 1:
            print(f"La función |g'(x)| >= 1 en x = {i}, el método puede no converger")
            return
        L = max(L, g_prime_value)
    print(f"La segunda condición se cumple: |g'(x)| < 1. L = {L}")

    # Calcular cantidad de iteraciones con L^n * |b - a| < error
    iteraciones = 0
    while L**iteraciones * abs(b - a) >= error:
        iteraciones += 1
    print(f"\nNúmero de iteraciones necesarias: {iteraciones}")

    # Función para realizar las iteraciones del método de punto fijo
    def iterar_punto_fijo(x0):
        current_x = x0
        for i in range(iteraciones):
            next_x = g_func(current_x)
            print(f"x{i+1} = {next_x}")
            current_x = next_x
        return next_x

    # Intentar con el extremo superior primero y luego el inferior
    for x0 in [b, a]:
        print(f"\nProbar con valor inicial x0 = {x0}")
        resultado = iterar_punto_fijo(x0)
        # Verificar si la iteración está suficientemente cerca del error deseado
        if abs(g_func(resultado) - resultado) < error:
            print(f"\nConvergió con x0 = {x0}")
            return resultado
        else:
            print(f"\nNo convergió con x0 = {x0}, intentando con el otro extremo")

    print("El método no converge con ninguno de los extremos.")
    return None

# Definir la función g(x)
def g_func(x):
    return (x**3 - 1) / 4

def main():
    # Parámetros
    intervalo = [-1, 1]  # Intervalo [a, b]
    error = 1e-5  # Error

    # Ejecutar el método de punto fijo
    resultado = punto_fijo(g_func, intervalo, error)
    print(f"\nResultado final después de las iteraciones: {resultado}")

if __name__ == "__main__":
    main()
