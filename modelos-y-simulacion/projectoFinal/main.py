import tkinter as tk
from tkinter import ttk, messagebox
import random
import numpy as np
import threading

# -----------------------------------------------------------------------------
# AJUSTES GENERALES DE LA INTERFAZ
CELL_SIZE = 60
FILAS = 6
COLUMNAS = 7
BOARD_WIDTH = COLUMNAS * CELL_SIZE
BOARD_HEIGHT = FILAS * CELL_SIZE

# -----------------------------------------------------------------------------
# FUNCIONES BÁSICAS DEL JUEGO CUATRO EN LINEA

def crear_tablero():
    return np.zeros((FILAS, COLUMNAS), dtype=int)

def movimiento_valido(tablero, col):
    return tablero[0][col] == 0

def obtener_fila_disponible(tablero, col):
    for f in range(FILAS - 1, -1, -1):
        if tablero[f][col] == 0:
            return f
    return None

def colocar_ficha(tablero, fila, col, ficha):
    tablero[fila][col] = ficha

def chequear_victoria(tablero, ficha):
    # Horizontal
    for r in range(FILAS):
        for c in range(COLUMNAS - 3):
            if (tablero[r][c] == ficha and
                    tablero[r][c+1] == ficha and
                    tablero[r][c+2] == ficha and
                    tablero[r][c+3] == ficha):
                return True

    # Vertical
    for c in range(COLUMNAS):
        for r in range(FILAS - 3):
            if (tablero[r][c] == ficha and
                    tablero[r+1][c] == ficha and
                    tablero[r+2][c] == ficha and
                    tablero[r+3][c] == ficha):
                return True

    # Diagonal \
    for r in range(FILAS - 3):
        for c in range(COLUMNAS - 3):
            if (tablero[r][c] == ficha and
                    tablero[r+1][c+1] == ficha and
                    tablero[r+2][c+2] == ficha and
                    tablero[r+3][c+3] == ficha):
                return True

    # Diagonal /
    for r in range(3, FILAS):
        for c in range(COLUMNAS - 3):
            if (tablero[r][c] == ficha and
                    tablero[r-1][c+1] == ficha and
                    tablero[r-2][c+2] == ficha and
                    tablero[r-3][c+3] == ficha):
                return True

    return False

def tablero_lleno(tablero):
    return all(tablero[0][c] != 0 for c in range(COLUMNAS))

# -----------------------------------------------------------------------------
# ESTRATEGIAS AUTOMÁTICAS

def estrategia_random(tablero, ficha):
    cols = [c for c in range(COLUMNAS) if movimiento_valido(tablero, c)]
    return random.choice(cols) if cols else None

def estrategia_blocker(tablero, ficha):
    oponente = 1 if ficha == 2 else 2
    # 1) buscar jugada ganadora
    for c in range(COLUMNAS):
        if movimiento_valido(tablero, c):
            f = obtener_fila_disponible(tablero, c)
            tablero[f][c] = ficha
            if chequear_victoria(tablero, ficha):
                tablero[f][c] = 0
                return c
            tablero[f][c] = 0
    # 2) bloquear
    for c in range(COLUMNAS):
        if movimiento_valido(tablero, c):
            f = obtener_fila_disponible(tablero, c)
            tablero[f][c] = oponente
            if chequear_victoria(tablero, oponente):
                tablero[f][c] = 0
                return c
            tablero[f][c] = 0
    # 3) random
    return estrategia_random(tablero, ficha)

def estrategia_ofensiva(tablero, ficha):
    # 1) buscar jugada ganadora
    for c in range(COLUMNAS):
        if movimiento_valido(tablero, c):
            f = obtener_fila_disponible(tablero, c)
            tablero[f][c] = ficha
            if chequear_victoria(tablero, ficha):
                tablero[f][c] = 0
                return c
            tablero[f][c] = 0
    # 2) random
    return estrategia_random(tablero, ficha)

def estrategia_central_focus(tablero, ficha):
    cols_validas = [c for c in range(COLUMNAS) if movimiento_valido(tablero, c)]
    if not cols_validas:
        return None
    pesos_base = [1, 2, 3, 4, 3, 2, 1]
    pesos = [pesos_base[c] if c in cols_validas else 0 for c in range(COLUMNAS)]
    total = sum(pesos)
    if total == 0:
        return estrategia_random(tablero, ficha)
    r = random.random()
    acumulado = 0
    for idx, p in enumerate(pesos):
        if p == 0:
            continue
        prob = p / total
        acumulado += prob
        if r <= acumulado:
            return idx
    return cols_validas[-1]

def simular_partida_rapida(tablero, ficha_inicial):
    turno = ficha_inicial
    while True:
        cols = [c for c in range(COLUMNAS) if movimiento_valido(tablero, c)]
        if not cols:
            return 0
        col = random.choice(cols)
        f = obtener_fila_disponible(tablero, col)
        tablero[f][col] = turno
        if chequear_victoria(tablero, turno):
            return turno
        if tablero_lleno(tablero):
            return 0
        turno = 1 if turno == 2 else 2

def estrategia_montecarlo(tablero, ficha, simulaciones=30):
    columnas_validas = [c for c in range(COLUMNAS) if movimiento_valido(tablero, c)]
    if not columnas_validas:
        return None
    mejor_columna = None
    mejor_winrate = -1
    oponente = 1 if ficha == 2 else 2

    for c in columnas_validas:
        f = obtener_fila_disponible(tablero, c)
        tablero[f][c] = ficha
        if chequear_victoria(tablero, ficha):
            tablero[f][c] = 0
            return c
        wins = 0
        for _ in range(simulaciones):
            copia = tablero.copy()
            ganador = simular_partida_rapida(copia, oponente)
            if ganador == ficha:
                wins += 1
        tablero[f][c] = 0
        winrate = wins / simulaciones
        if winrate > mejor_winrate:
            mejor_winrate = winrate
            mejor_columna = c

    return mejor_columna if mejor_columna is not None else estrategia_random(tablero, ficha)

def estrategia_hybrid(tablero, ficha):
    oponente = 1 if ficha == 2 else 2
    # 1) jugada ganadora
    for c in range(COLUMNAS):
        if movimiento_valido(tablero, c):
            f = obtener_fila_disponible(tablero, c)
            tablero[f][c] = ficha
            if chequear_victoria(tablero, ficha):
                tablero[f][c] = 0
                return c
            tablero[f][c] = 0
    # 2) bloquear
    for c in range(COLUMNAS):
        if movimiento_valido(tablero, c):
            f = obtener_fila_disponible(tablero, c)
            tablero[f][c] = oponente
            if chequear_victoria(tablero, oponente):
                tablero[f][c] = 0
                return c
            tablero[f][c] = 0
    # 3) pequeño montecarlo
    return estrategia_montecarlo(tablero, ficha, simulaciones=10)

# -----------------------------------------------------------------------------
# ESTRATEGIAS: se añade "Human" como opción especial para la partida visual
ESTRATEGIAS = {
    "Human": None,
    "Random": estrategia_random,
    "Blocker": estrategia_blocker,
    "Offensive": estrategia_ofensiva,
    "CentralFocus": estrategia_central_focus,
    "Montecarlo": estrategia_montecarlo,
    "Hybrid": estrategia_hybrid
}

# -----------------------------------------------------------------------------
# PARTIDA SIN VISUAL (para simulaciones en bloque)
def jugar_partida_sin_visual(estrategia1, estrategia2):
    tablero = crear_tablero()
    turno = 1
    while True:
        if turno == 1:
            col = estrategia1(tablero, 1)
            if col is None:
                return 0 # Sin movidas válidas o "Human" => Empate
            f = obtener_fila_disponible(tablero, col)
            colocar_ficha(tablero, f, col, 1)
            if chequear_victoria(tablero, 1):
                return 1
        else:
            col = estrategia2(tablero, 2)
            if col is None:
                return 0
            f = obtener_fila_disponible(tablero, col)
            colocar_ficha(tablero, f, col, 2)
            if chequear_victoria(tablero, 2):
                return 2

        if tablero_lleno(tablero):
            return 0
        turno = 1 if turno == 2 else 2

# -----------------------------------------------------------------------------
# TORNEO (round robin) => victorias y empates
def simular_estrategias(seleccionadas, n_partidas, progress_bar, root):
    # Diccionarios de conteo
    victorias = {name: {op: 0 for op in seleccionadas} for name in seleccionadas}
    empates = {name: {op: 0 for op in seleccionadas} for name in seleccionadas}

    # Calcular total
    n_estr = len(seleccionadas)
    total_pairs = (n_estr * (n_estr - 1)) // 2
    total_matches = total_pairs * n_partidas
    progress_bar["maximum"] = total_matches
    progress_bar["value"] = 0

    current_match = 0

    for i in range(n_estr):
        for j in range(i+1, n_estr):
            e1 = seleccionadas[i]
            e2 = seleccionadas[j]
            estrategia1 = ESTRATEGIAS[e1]
            estrategia2 = ESTRATEGIAS[e2]

            # Jugamos n_partidas con e1=Jugador1 y e2=Jugador2
            for _ in range(n_partidas):
                if estrategia1 and estrategia2:
                    ganador = jugar_partida_sin_visual(estrategia1, estrategia2)
                else:
                    # Si alguno es None (Human), se fuerza empate
                    ganador = 0

                if ganador == 1:
                    victorias[e1][e2] += 1
                elif ganador == 2:
                    victorias[e2][e1] += 1
                else:
                    empates[e1][e2] += 1
                    empates[e2][e1] += 1

                current_match += 1
                progress_bar["value"] = current_match
                root.update_idletasks()

    return victorias, empates

# -----------------------------------------------------------------------------
# MOSTRAR RESULTADOS EN TABLAS (Treeview)
def mostrar_resultados_en_tablas(root, seleccionadas, victorias, empates):
    results_window = tk.Toplevel(root)
    results_window.title("Resultados del Torneo")

    label_vic = tk.Label(results_window, text="Victorias")
    label_vic.pack(pady=5)

    tree_vic = ttk.Treeview(results_window,
                            columns=["Strategy"] + seleccionadas,
                            show='headings',
                            height=len(seleccionadas)+1)
    tree_vic.pack(padx=5, pady=5)

    tree_vic.heading("Strategy", text="Strategy")
    tree_vic.column("Strategy", width=100, anchor=tk.CENTER)
    for st in seleccionadas:
        tree_vic.heading(st, text=st)
        tree_vic.column(st, width=50, anchor=tk.CENTER)

    for e1 in seleccionadas:
        row_data = []
        for e2 in seleccionadas:
            if e1 == e2:
                row_data.append("-")
            else:
                row_data.append(str(victorias[e1][e2]))
        tree_vic.insert('', tk.END, values=[e1] + row_data)

    label_emp = tk.Label(results_window, text="Empates")
    label_emp.pack(pady=5)

    tree_emp = ttk.Treeview(results_window,
                            columns=["Strategy"] + seleccionadas,
                            show='headings',
                            height=len(seleccionadas)+1)
    tree_emp.pack(padx=5, pady=5)

    tree_emp.heading("Strategy", text="Strategy")
    tree_emp.column("Strategy", width=100, anchor=tk.CENTER)
    for st in seleccionadas:
        tree_emp.heading(st, text=st)
        tree_emp.column(st, width=50, anchor=tk.CENTER)

    for e1 in seleccionadas:
        row_data = []
        for e2 in seleccionadas:
            if e1 == e2:
                row_data.append("-")
            else:
                row_data.append(str(empates[e1][e2]))
        tree_emp.insert('', tk.END, values=[e1] + row_data)

# -----------------------------------------------------------------------------
# DIBUJAR TABLERO (canvas)
def dibujar_tablero(canvas, tablero):
    canvas.delete("all")
    for r in range(FILAS):
        for c in range(COLUMNAS):
            x1 = c * CELL_SIZE
            y1 = r * CELL_SIZE
            x2 = x1 + CELL_SIZE
            y2 = y1 + CELL_SIZE
            canvas.create_rectangle(x1, y1, x2, y2, fill="blue", outline="black")

            if tablero[r][c] == 0:
                color = "white"
            elif tablero[r][c] == 1:
                color = "red"
            else:
                color = "yellow"

            cx = x1 + CELL_SIZE/2
            cy = y1 + CELL_SIZE/2
            r_circle = CELL_SIZE/2 - 4
            canvas.create_oval(cx-r_circle, cy-r_circle, cx+r_circle, cy+r_circle,
                               fill=color, outline="black")

# -----------------------------------------------------------------------------
# CLASE PRINCIPAL DE LA APLICACIÓN
class CuatroEnLinea:
    def __init__(self, root):
        self.root = root
        root.title("4 En Linea - Jugar Humano vs IA o IA vs IA")

        # ---------------------------------------------------------------------
        # FRAME SUPERIOR: selección para partida visual
        frame_partida = ttk.LabelFrame(root, text="Partida Visual (Humano vs IA / IA vs IA)")
        frame_partida.pack(side=tk.TOP, fill=tk.X, padx=5, pady=5)

        ttk.Label(frame_partida, text="Estrategia Jugador 1:").pack(side=tk.LEFT, padx=5)
        self.combo_j1 = ttk.Combobox(frame_partida, values=list(ESTRATEGIAS.keys()), state="readonly")
        self.combo_j1.pack(side=tk.LEFT, padx=5)
        self.combo_j1.current(0) # por defecto: "Human"

        ttk.Label(frame_partida, text="Estrategia Jugador 2:").pack(side=tk.LEFT, padx=5)
        self.combo_j2 = ttk.Combobox(frame_partida, values=list(ESTRATEGIAS.keys()), state="readonly")
        self.combo_j2.pack(side=tk.LEFT, padx=5)
        self.combo_j2.current(1) # por defecto: "Random"

        self.btn_jugar = ttk.Button(frame_partida, text="Iniciar Partida", command=self.iniciar_partida_visual)
        self.btn_jugar.pack(side=tk.LEFT, padx=5)

        # ---------------------------------------------------------------------
        # FRAME INTERMEDIO: tablero a la izquierda + log a la derecha
        main_frame = ttk.Frame(root)
        main_frame.pack(side=tk.TOP, fill=tk.BOTH, expand=True)

        board_frame = ttk.Frame(main_frame)
        board_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

        self.canvas = tk.Canvas(board_frame, width=BOARD_WIDTH, height=BOARD_HEIGHT, bg="white")
        self.canvas.pack(side=tk.LEFT, padx=5, pady=5)

        # Bind de click en el canvas (para jugadas humanas)
        self.canvas.bind("<Button-1>", self.on_canvas_click)

        log_frame = ttk.Frame(main_frame)
        log_frame.pack(side=tk.RIGHT, fill=tk.Y)

        ttk.Label(log_frame, text="Registro de jugadas:").pack(anchor=tk.NW, padx=5, pady=5)
        self.log_text = tk.Text(log_frame, width=40, height=20)
        self.log_text.pack(side=tk.TOP, fill=tk.BOTH, padx=5, pady=5)

        # Tablero inicial
        self.tablero_actual = crear_tablero()
        dibujar_tablero(self.canvas, self.tablero_actual)

        # Para controlar la partida visual
        self.partida_en_curso = False
        self.current_player = 1
        self.jug1_name = None
        self.jug2_name = None

        # ---------------------------------------------------------------------
        # FRAME INFERIOR: simulaciones (torneo), sin "Human"
        frame_sim = ttk.LabelFrame(root, text="Simulaciones Montecarlo (IA vs IA)")
        frame_sim.pack(side=tk.TOP, fill=tk.X, padx=5, pady=5)

        ttk.Label(frame_sim, text="N Partidas por Enfrentamiento:").pack(side=tk.LEFT, padx=5)
        self.entry_n = ttk.Entry(frame_sim, width=5)
        self.entry_n.insert(0, "50")
        self.entry_n.pack(side=tk.LEFT, padx=5)

        self.vars_estrategias = {}
        # Aquí EXCLUIMOS "Human"
        for name in ESTRATEGIAS.keys():
            if name == "Human":
                continue # no lo mostramos en los checkbuttons
            var = tk.BooleanVar(value=True)
            cb = ttk.Checkbutton(frame_sim, text=name, variable=var)
            cb.pack(side=tk.LEFT, padx=2)
            self.vars_estrategias[name] = var

        self.btn_simular = ttk.Button(frame_sim, text="Simular Torneo", command=self.simular_torneo_thread)
        self.btn_simular.pack(side=tk.LEFT, padx=5)

        self.progress_bar = ttk.Progressbar(frame_sim, orient="horizontal", length=200, mode="determinate")
        self.progress_bar.pack(side=tk.LEFT, padx=10)

    # -------------------------------------------------------------------------
    # LOGICA PARA PARTIDA VISUAL

    def iniciar_partida_visual(self):
        """Se configura e inicia una partida (sea humano vs IA o IA vs IA)."""
        self.tablero_actual = crear_tablero()
        dibujar_tablero(self.canvas, self.tablero_actual)
        self.log_text.delete("1.0", tk.END)

        self.jug1_name = self.combo_j1.get()
        self.jug2_name = self.combo_j2.get()

        self.current_player = 1
        self.partida_en_curso = True

        self.log_text.insert(tk.END, f"Inicia partida: Jug1={self.jug1_name}, Jug2={self.jug2_name}\n")

        # Empezamos la secuencia de movimientos
        self.next_move()

    def on_canvas_click(self, event):
        """
        Maneja el click del usuario en el tablero.
        Si es el turno de un jugador Humano, se intenta colocar ficha
        en la columna clicada.
        """
        if not self.partida_en_curso:
            return

        # ¿Es turno de un humano?
        current_strategy_name = self.jug1_name if self.current_player == 1 else self.jug2_name
        if current_strategy_name != "Human":
            return # No es humano, ignorar clicks

        col = event.x // CELL_SIZE
        if col < 0 or col >= COLUMNAS:
            return
        if not movimiento_valido(self.tablero_actual, col):
            self.log_text.insert(tk.END, "Columna llena o inválida, elige otra.\n")
            self.log_text.see(tk.END)
            return

        # Realizamos la jugada
        fila = obtener_fila_disponible(self.tablero_actual, col)
        colocar_ficha(self.tablero_actual, fila, col, self.current_player)
        dibujar_tablero(self.canvas, self.tablero_actual)
        self.log_text.insert(tk.END, f"Jugador {self.current_player} (Human) -> col {col}\n")
        self.log_text.see(tk.END)

        if chequear_victoria(self.tablero_actual, self.current_player):
            self.log_text.insert(tk.END, f"¡¡Gana Jugador {self.current_player} (Human)!!\n")
            self.log_text.see(tk.END)
            self.partida_en_curso = False
            return
        if tablero_lleno(self.tablero_actual):
            self.log_text.insert(tk.END, "¡Empate! Tablero lleno\n")
            self.partida_en_curso = False
            return

        self.current_player = 1 if self.current_player == 2 else 2
        self.next_move()

    def next_move(self):
        """
        Efectúa la jugada del siguiente jugador si es IA.
        Si es "Human", esperamos a on_canvas_click.
        """
        if not self.partida_en_curso:
            return

        current_strategy_name = self.jug1_name if self.current_player == 1 else self.jug2_name

        # Si es "Human", esperamos click
        if current_strategy_name == "Human":
            return

        # Es IA
        strategy_func = ESTRATEGIAS[current_strategy_name]
        if strategy_func is None:
            return

        col = strategy_func(self.tablero_actual, self.current_player)
        if col is None:
            self.log_text.insert(tk.END, "No hay movimientos válidos. Empate.\n")
            self.partida_en_curso = False
            return

        fila = obtener_fila_disponible(self.tablero_actual, col)
        colocar_ficha(self.tablero_actual, fila, col, self.current_player)
        dibujar_tablero(self.canvas, self.tablero_actual)
        self.log_text.insert(tk.END, f"Jugador {self.current_player} ({current_strategy_name}) -> col {col}\n")
        self.log_text.see(tk.END)

        if chequear_victoria(self.tablero_actual, self.current_player):
            self.log_text.insert(tk.END, f"¡¡Gana Jugador {self.current_player} ({current_strategy_name})!!\n")
            self.partida_en_curso = False
            return

        if tablero_lleno(self.tablero_actual):
            self.log_text.insert(tk.END, "¡Empate! Tablero lleno\n")
            self.partida_en_curso = False
            return

        self.current_player = 1 if self.current_player == 2 else 2
        self.root.after(300, self.next_move) # Breve delay (0.3s) entre movimientos IA

    # -------------------------------------------------------------------------
    # SIMULACIÓN TIPO TORNEO (IA vs IA)
    def simular_torneo_thread(self):
        def run_sim():
            seleccionadas = [k for k, v in self.vars_estrategias.items() if v.get()]
            if len(seleccionadas) < 2:
                messagebox.showinfo("Info", "Debes seleccionar al menos 2 estrategias.")
                return
            try:
                n = int(self.entry_n.get())
            except ValueError:
                n = 10

            victorias, empates = simular_estrategias(seleccionadas, n, self.progress_bar, self.root)
            mostrar_resultados_en_tablas(self.root, seleccionadas, victorias, empates)

        hilo = threading.Thread(target=run_sim)
        hilo.start()

# -----------------------------------------------------------------------------
# MAIN
if __name__ == "__main__":
    root = tk.Tk()
    app = CuatroEnLinea(root)
    root.mainloop()