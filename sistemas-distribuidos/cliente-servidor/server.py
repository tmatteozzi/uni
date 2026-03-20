"""
SERVIDOR DE CALCULADORA - TCP/IP CON SOCKETS
SOLO SOPORTA: +, -, *, /, ^ y paréntesis
RESPETA EL ORDEN DE PRECEDENCIA DE OPERADORES.
"""

import socket
import re

HOST = '127.0.0.1'
PORT = 65432

# FUNCION PARA EVALUAR LA EXPRESION MATEMATICA DE FORMA SEGURA
def safe_eval(expression: str) -> float:
    # REMPLAZAR ^ POR ** PARA POTENCIA EN PYTHON
    expression = expression.replace('^', '**')

    # VALIDAR QUE LA EXPRESION SOLO CONTENGA CARACTERES SEGUROS
    allowed = re.compile(r'^[\d\s\+\-\*\/\(\)\.\*]+$')
    if not allowed.match(expression):
        raise ValueError("Expresión contiene caracteres no permitidos.")

    # EVALUAR CON EVAL LIMITADO (SIN BUILTINS)
    result = eval(expression, {"__builtins__": {}}, {})
    return result

# FUNCION PARA MANEJAR LA CONEXION DEL CLIENTE
def handle_client(conn, addr):
    print(f"[CONEXIÓN] Cliente conectado desde {addr}")
    with conn:
        while True:
            data = conn.recv(1024)
            if not data:
                break
            # DECODIFICAR LA EXPRESION RECIBIDA
            expression = data.decode('utf-8').strip()
            print(f"[RECIBIDO] Expresión: {expression}")

            # EVALUAR LA EXPRESION MATEMATICA
            try:
                result = safe_eval(expression)
                # FORMATEAR: SI ES ENTERO, MOSTRAR SIN DECIMALES
                if isinstance(result, float) and result.is_integer():
                    response = str(int(result))
                else:
                    response = f"{result:.10g}"
            except ZeroDivisionError:
                response = "ERROR: División por cero"
            except Exception as e:
                response = f"ERROR: {str(e)}"

            print(f"[ENVIADO]  Resultado: {response}")
            conn.sendall(response.encode('utf-8'))

    print(f"[DESCONEXIÓN] Cliente {addr} desconectado.")

# FUNCION PARA INICIAR EL SERVIDOR
def start_server():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: # CREA UN SOCKET TCP
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) # CONFIGURA EL SOCKET PARA REUTILIZAR EL PUERTO
        s.bind((HOST, PORT)) # ASIGNA UNA DIRECCION IP Y UN PUERTO AL SOCKET
        s.listen() # PONE EL SOCKET EN MODO ESCUCHA
        print(f"[SERVIDOR] Escuchando en {HOST}:{PORT} ...")
        print("[SERVIDOR] Esperando conexiones (Ctrl+C para detener)\n")

        while True:
            conn, addr = s.accept() # ACEPTO LA CONEXION DEL CLIENTE
            handle_client(conn, addr) # MANEJO LA CONEXION DEL CLIENTE


if __name__ == '__main__':
    start_server()