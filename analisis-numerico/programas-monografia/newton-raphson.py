import sympy as sp

def verificar_condiciones(funcion, derivada1, derivada2, a, b):
    x = sp.symbols('x')

    # Verificar continuidad de f, f', f''
    try:
        sp.simplify(funcion)
        sp.simplify(derivada1)
        sp.simplify(derivada2)
    except Exception as e:
        raise ValueError(f"Error al simplificar las funciones: {e}")

    # Verificar que f(a) * f(b) < 0
    f_a = funcion.subs(x, a)
    f_b = funcion.subs(x, b)
    if f_a * f_b >= 0:
        raise ValueError("f(a) * f(b) debe ser menor que 0")

    # Verificar que f'(x) != 0 en [a, b]
    for i in range(11):
        xi = a + i * (b - a) / 10
        if derivada1.subs(x, xi) == 0:
            raise ValueError("f'(x) no debe ser cero en el intervalo [a, b]")

def encontrar_intervalo(funcion, x, start=0, step=1, max_iter=100):
    a = start
    b = a + step
    for _ in range(max_iter):
        f_a = float(funcion.subs(x, a))
        f_b = float(funcion.subs(x, b))

        if f_a * f_b < 0:
            return a, b

        # Ajustar dinámicamente el tamaño del paso si no se encuentra un intervalo
        a = b
        step *= 1.5  # Incrementa el tamaño del paso exponencialmente para cubrir más rango
        b += step

    raise ValueError("No se encontró un intervalo adecuado.")

def newton_raphson(funcion_str, decimales):
    x = sp.symbols('x')
    funcion = sp.sympify(funcion_str)
    derivada1 = sp.diff(funcion, x)
    derivada2 = sp.diff(derivada1, x)

    # Encontrar un intervalo adecuado
    a, b = encontrar_intervalo(funcion, x)

    # Mostrar los intervalos que se utilizan
    print(f"Intervalo encontrado: [{a}, {b}]")

    # Verificar condiciones
    verificar_condiciones(funcion, derivada1, derivada2, a, b)

    # Inicializar x0
    f_a = float(funcion.subs(x, a))
    f_b = float(funcion.subs(x, b))
    f_pp_a = float(derivada2.subs(x, a))
    f_pp_b = float(derivada2.subs(x, b))

    if f_a * f_pp_a > 0:
        x0 = a
    elif f_b * f_pp_b > 0:
        x0 = b
    else:
        raise ValueError("No se puede seleccionar x0 de acuerdo a las condiciones dadas")

    precision = 10**(-decimales)  # Umbral de precisión basado en los decimales
    iteraciones = 0

    print(f"x{iteraciones} = {x0:.{decimales}f}")
    while True:
        f_x0 = float(funcion.subs(x, x0))
        f_p_x0 = float(derivada1.subs(x, x0))

        if f_p_x0 == 0:
            raise ValueError("La derivada es cero en x0, el método no puede continuar")

        x1 = x0 - f_x0 / f_p_x0

        # Imprimir iteración
        iteraciones += 1
        print(f"x{iteraciones} = {x1:.{decimales}f}")

        if abs(x1 - x0) < precision:
            break

        x0 = x1

# EJEMPLO DE USO
def main():
    funcion = 'exp(x) - (x - 2)**2'
    decimales = 6

    print(f"Función: {funcion}")
    print(f"Decimales exactos: {decimales}\n")

    newton_raphson(funcion, decimales)

if __name__ == "__main__":
    main()
