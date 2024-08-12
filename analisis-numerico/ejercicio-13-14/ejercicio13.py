import math

# f(x) = e^x
def calcular_exponencial(x):
    return math.exp(x)

# Calcular la derivada aproximada de f(x) en x
def calcular_derivada_aproximada(punto, particion):
    return (calcular_exponencial(punto + particion) - calcular_exponencial(punto)) / particion

# Punto en el que se evaluará la derivada
punto_evaluacion = 0

# Valor exacto de la derivada en el punto de evaluación
valor_exacto = calcular_exponencial(punto_evaluacion)

# Listas para almacenar los valores de h, las aproximaciones, el error absoluto y el error relativo
lista_particiones = []
lista_aproximaciones = []
lista_errores_absolutos = []
lista_errores_relativos = []

# Calcular para cada valor de i
for i in range(1, 31):
    particion = 2**-i
    aproximacion = calcular_derivada_aproximada(punto_evaluacion, particion)
    error_absoluto = abs(valor_exacto - aproximacion)
    error_relativo = (error_absoluto / valor_exacto) * 100  # Porcentaje

    # Guardar los valores en las listas
    lista_particiones.append(particion)
    lista_aproximaciones.append(aproximacion)
    lista_errores_absolutos.append(error_absoluto)
    lista_errores_relativos.append(error_relativo)

# Imprimir la tabla de resultados
print(f"{'i':>2} {'Partición (h)':>20} {'Aproximación':>20} {'Error Absoluto':>20} {'Error Relativo (%)':>20}")
print("-" * 85)
for i in range(30):
    print(f"{i+1:2} {lista_particiones[i]:>20.15f} {lista_aproximaciones[i]:>20.15f} {lista_errores_absolutos[i]:>20.15f} {lista_errores_relativos[i]:>20.15f}")