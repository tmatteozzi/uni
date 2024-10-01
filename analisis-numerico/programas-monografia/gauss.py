import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import least_squares

# 1. Parámetros orbitales reales de Ceres (valores ficticios para este ejemplo)
semieje_mayor_real = 2.77  # Semieje mayor en UA (unidades astronómicas)
excentricidad_real = 0.08  # Excentricidad
argumento_perihelio_real = np.deg2rad(73)  # Argumento del perihelio en radianes

# 2. Generación de datos de observación simulados
n_puntos = 100
theta = np.linspace(0, 2 * np.pi, n_puntos)  # Ángulo theta para diferentes posiciones orbitales
radio = (semieje_mayor_real * (1 - excentricidad_real**2)) / (1 + excentricidad_real * np.cos(theta - argumento_perihelio_real))
x_real = radio * np.cos(theta)
y_real = radio * np.sin(theta)

# Añadir ruido a los datos simulados (para simular observaciones imperfectas)
nivel_ruido = 0.05
x_observado = x_real + nivel_ruido * np.random.randn(n_puntos)
y_observado = y_real + nivel_ruido * np.random.randn(n_puntos)

# 3. Definir el modelo orbital
def modelo_orbital(parametros, theta):
    semieje_mayor, excentricidad, argumento_perihelio = parametros
    radio = (semieje_mayor * (1 - excentricidad**2)) / (1 + excentricidad * np.cos(theta - argumento_perihelio))
    x_modelo = radio * np.cos(theta)
    y_modelo = radio * np.sin(theta)
    return x_modelo, y_modelo

# 4. Definir la función de residuos (errores)
def residuos(parametros, theta, x_observado, y_observado):
    x_modelo, y_modelo = modelo_orbital(parametros, theta)
    error_x = x_modelo - x_observado
    error_y = y_modelo - y_observado
    return np.concatenate([error_x, error_y])

# 5. Valores iniciales para los parámetros y ajuste mediante mínimos cuadrados
parametros_iniciales = [2.5, 0.1, np.deg2rad(60)]  # Estimaciones iniciales para el ajuste
resultado = least_squares(
    residuos, parametros_iniciales, args=(theta, x_observado, y_observado),
    bounds=([0, 0, 0], [np.inf, 1, 2 * np.pi]),  # Restricciones: parámetros físicamente válidos
    verbose=2
)

# 6. Parámetros ajustados
semieje_mayor_ajustado, excentricidad_ajustada, argumento_perihelio_ajustado = resultado.x
print(f"Semieje mayor ajustado (a): {semieje_mayor_ajustado}")
print(f"Excentricidad ajustada (e): {excentricidad_ajustada}")
print(f"Argumento del perihelio ajustado (omega) en grados: {np.rad2deg(argumento_perihelio_ajustado)}")

# 7. Generar modelo ajustado con los parámetros obtenidos
x_ajustado, y_ajustado = modelo_orbital(resultado.x, theta)

# 8. Graficar resultados: datos observados, modelo ajustado y órbita real
plt.figure(figsize=(8, 6))
plt.plot(x_observado, y_observado, 'o', label='Datos observados', alpha=0.5)
plt.plot(x_ajustado, y_ajustado, '-', label='Modelo ajustado', linewidth=2)
plt.plot(x_real, y_real, '--', label='Órbita real', linewidth=1)
plt.xlabel('X (UA)')
plt.ylabel('Y (UA)')
plt.legend()
plt.axis('equal')
plt.title('Ajuste de Órbita Elíptica para Ceres usando Mínimos Cuadrados')
plt.show()

# 9. Análisis de los resultados
residuos_finales = residuos(resultado.x, theta, x_observado, y_observado)
suma_residuos_cuadrados = np.sum(residuos_finales**2)
print(f"Suma de los cuadrados de los residuos: {suma_residuos_cuadrados}")
print(f"Diferencia en semieje mayor: {semieje_mayor_ajustado - semieje_mayor_real}")
print(f"Diferencia en excentricidad: {excentricidad_ajustada - excentricidad_real}")
print(f"Diferencia en omega (grados): {np.rad2deg(argumento_perihelio_ajustado - argumento_perihelio_real)}")