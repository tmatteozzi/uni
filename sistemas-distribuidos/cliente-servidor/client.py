"""
CLIENTE DE CALCULADORA - TCP/IP CON SOCKETS
ENVIA EXPRESIONES MATEMATICAS AL SERVIDOR Y MUESTRA LOS RESULTADOS.
"""

import socket

HOST = '127.0.0.1'
PORT = 65432

# FUNCION PARA ENVIAR LA EXPRESION AL SERVIDOR Y RECIBIR EL RESULTADO
def send_expression(expression: str) -> str:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: # CREA UN SOCKET TCP
        s.connect((HOST, PORT)) # CONECTA AL SERVIDOR
        s.sendall(expression.encode('utf-8')) # ENVIA LA EXPRESION AL SERVIDOR
        result = s.recv(1024).decode('utf-8') # RECIBE EL RESULTADO DEL SERVIDOR
    return result

# FUNCION PARA EJECUTAR LOS CASOS DE PRUEBA DEL TP
def run_test_cases():
    test_cases = [
        "1+2+3+4+5+6+7+8+9+10",
        "1*2+3^4-5*6+7/8+9/10",
        "1*(2+3^4)-5*(6+7)/(8+9/10)",
        "1*(2+3^4)-5*((6+7)/(8+9/10))",
    ]

    print("=" * 60)
    print("   CALCULADORA CLIENTE/SERVIDOR — CASOS DE PRUEBA")
    print("=" * 60)

    for expr in test_cases:
        result = send_expression(expr)
        print(f"\n  Expresión : {expr}")
        print(f"  Resultado : {result}")

    print("\n" + "=" * 60)

# FUNCION PARA EJECUTAR EL MODO INTERACTIVO
def interactive_mode():
    print("\n" + "=" * 60)
    print("   MODO INTERACTIVO")
    print("   Ingrese una expresión matemática o 'salir' para terminar.")
    print("   Operadores: +  -  *  /  ^  ( )")
    print("=" * 60)

    while True:
        expr = input("\n  >> Expresión: ").strip()
        if expr.lower() in ('salir', 'exit', 'quit'):
            print("  Cerrando cliente. ¡Hasta luego!")
            break
        if not expr:
            continue
        try:
            result = send_expression(expr)
            print(f"     Resultado: {result}")
        except ConnectionRefusedError:
            print("  ERROR: No se pudo conectar al servidor. ¿Está en ejecución?")
        except Exception as e:
            print(f"  ERROR inesperado: {e}")


if __name__ == '__main__':
    import sys

    if len(sys.argv) > 1 and sys.argv[1] == '--test':
        # EJECUTA LOS CASOS DE PRUEBA
        try:
            run_test_cases()
        except ConnectionRefusedError:
            print("ERROR: No se pudo conectar al servidor. Asegurese de que server.py este corriendo.")
    else:
        # EJECUTA LOS CASOS DE PRUEBA Y EL MODO INTERACTIVO
        try:
            run_test_cases()
            interactive_mode()
        except ConnectionRefusedError:
            print("ERROR: No se pudo conectar al servidor. Asegurese de que server.py este corriendo.")