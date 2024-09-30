import sympy as sp

def newton_raphson_van_der_waals(p, T, R, a, b, decimales):
    # v -> VOLUMEN MOLAR
    v = sp.symbols('v')

    # ECUACION DE VAN DER WAALS
    funcion = (p + a / v**2) * (v - b) - R * T

    # DERIVADO
    derivada1 = sp.diff(funcion, v)

    # METODO DE NEWTON RAPHSON
    def newton_raphson(funcion, derivada1, v0, decimales):
        precision = 10**(-decimales)
        iteraciones = 0

        print(f"v{iteraciones} = {v0:.{decimales}f}")
        while True:
            f_v0 = float(funcion.subs(v, v0))
            f_p_v0 = float(derivada1.subs(v, v0))

            if f_p_v0 == 0:
                raise ValueError("La derivada es cero en v0, el método no puede continuar")

            v1 = v0 - f_v0 / f_p_v0

            iteraciones += 1
            print(f"v{iteraciones} = {v1:.{decimales}f}")

            if abs(v1 - v0) < precision:
                break

            v0 = v1

    # USAR UN VALOR INICIAL BASADO EN LA LEY DE LOS GASES IDEALES
    v0 = R * T / p

    # MOSTRAR ECUACION Y DERIVADA
    print(f"Ecuación de Van der Waals: {funcion}")
    print(f"Derivada de la ecuación: {derivada1}\n")

    # APLICAR NEWTON RAPHSON
    newton_raphson(funcion, derivada1, v0, decimales)

# EJEMPLO DE USO
def main():
    p = 1  # PRESION EN ATMOSFERAS
    T = 300  # TEMPERATURA EN KELVIN
    R = 0.082054  # CONSTANTE DE LOS GASES EN L·atm/(mol·K)
    a = 3.592  # CONSTANTE PARA CO2
    b = 0.04267  # CONSTANTE PARA CO2
    decimales = 6 # DECIMALES DE PRECISION

    print(f"Calculando volumen molar para CO2 usando la ecuación de Van der Waals\n")
    newton_raphson_van_der_waals(p, T, R, a, b, decimales)

if __name__ == "__main__":
    main()