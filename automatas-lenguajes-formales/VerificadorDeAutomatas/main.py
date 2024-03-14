# w == cadena
# c == simbolo
# Q == conj de estados
# E == alfabeto
# q0 == estado inicial
# f == funcion de transicion
# F == conj de estados finales
# automata -> objeto compuesto por (Q, E, q0, f, F)

def afd(automata, w):
    # Desempaquetar el automata
    Q, E, q0, f, F = automata

    # Comenzar en el estado inicial
    estado = q0

    # Diccionario para mapear símbolos a índices
    cAIndice = {simbolo: i for i, simbolo in enumerate(E)}

    # Recorrer la cadena simbolo a simbolo (c == simbolo)
    for c in w:
        # Obtener el índice del símbolo actual
        indiceC = cAIndice[c]
        siguienteQ = f[estado][indiceC]
        # Si el siguiente estado es None, la cadena no pertenece al automata
        if siguienteQ is None:
            return False
        # Actualizar el estado actual
        estado = siguienteQ

    # La cadena pertenece al automata si el estado final está en el conjunto de estados finales
    return estado in F


# Ejemplo de uso
automata = (
    # Q
    [0, 1, 2, 3],
    # E
    ["a", "b", "c"],
    # q0
    0,
    # f
    [[1, None, None],
     [None, 2, None],
     [None, None, 3],
     [None, None, None]],
    # F
    [0, 1, 2, 3]
)

cadena = "abc"

resultado = afd(automata, cadena)

if resultado:
    print(f"La cadena '{cadena}' pertenece al automata")
else:
    print(f"La cadena '{cadena}' no pertence al automata")
