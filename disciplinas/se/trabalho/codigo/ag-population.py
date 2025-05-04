# -*- coding: utf-8 -*-
"""
Created: 15/01/2014
@autor: Alexandre Costa
@disciplina: Sistemas Evolutivos
@professor: Marilton Aguiar
@descrição: trabalho final da @disciplina, onde foi desenvolvido um Algoritmo Genético que simula o ataque de pragas periódicas a um conjunto de colonias.
"""

# Packages
from Tkinter import *
import random
import time
from datetime import datetime

# GLOBAL



# --------------------------------------------- #
def def_var():
    # DECLARA VARIAVEIS
    ## Variaveis do automato celular
    global N # Lattice size (N×N)
    global l # tamanho maximo do youth (y)
    global m # tamanho maximo do maturiN (m)
    global n # tamanho maximo do old (o)
    global L # tamanho do "Código Genético"
    global alpha # "Código Genético"
    global MAX # maximo de gerações (tempo maximo)
    global a
    global k
    global populacao # Numero total de invíduos a cada iteração

    ## Variaveis da praga
    global dose
    global plaguePeriod

    ## Variaveis para desenho no ecran
    global XY # x0, y0, x1, y1
    global root
    global canvas
    global cell
    global N
    global stop

# --------------------------------------------- #
def callback(a, aux_cont):
    aux_cont = aux_cont + 1
    print "Tempo = %d" %(aux_cont)
    a, populacao = geracao(a, aux_cont)
    destroy_bt()
    monta(a, aux_cont)

# --------------------------------------------- #
def callback_2(a, aux_cont):
    global stop
    global MAX

    aux_cont = aux_cont + 1
    a, populacao = geracao(a, aux_cont)
    print "-----------------------------"
    print "Tempo = %d" %(aux_cont)
    print "População Atual = %d" %(populacao)
    print "-----------------------------"
    monta(a, aux_cont)
    destroy_bt()
    if aux_cont < MAX and stop == 0:
        root.after(50,lambda: callback_2(a, aux_cont))



# --------------------------------------------- #
def callback_3(a):
    global stop
    stop = 1


# --------------------------------------------- #
def destroy_bt():
    bt1.destroy()
    bt2.destroy()
    bt3.destroy()

# --------------------------------------------- #
def monta(a, aux_cont):
    for j in range(N):
        for i in range(N):
            if a[i][j] == 0:
                canvas.itemconfig(cell[i][j], fill="white")
            elif a[i][j] == 1:
                ageYouth, ageMaturiN, ageOld = p(alpha[i][j])
                if k[i][j] <= ageYouth:
                        canvas.itemconfig(cell[i][j], fill="yellow")
                elif k[i][j] > ageYouth and k[i][j] <= (ageYouth + ageMaturiN):
                        canvas.itemconfig(cell[i][j], fill="red")
                else:
                        canvas.itemconfig(cell[i][j], fill="blue")
            else:
                canvas.itemconfig(cell[i][j], fill="green")

# (Re)define Botões
    global bt1
    global bt2
    global bt3

    bt1 = Button(root, text=">", command=lambda: callback(a, aux_cont))
    bt2 = Button(root, text="PLAY", command=lambda: callback_2(a, aux_cont))
    bt3 = Button(root, text="||", command=lambda: callback_3(a))
    canvas.pack()

    bt3.pack(side=LEFT)
    bt2.pack(side=LEFT)
    bt1.pack(side=LEFT)

    return


# --------------------------------------------- #
def geracao(a, aux_cont):
    encontrou = 0
    contaAlpha = 0
    for j in range(N):
        for i in range(N):
            if a[i][j] == 0:
                alpha1, alpha2, encontrou = find_two_mature_neighbors(i, j)
                if encontrou == 2:
                    a[i][j] = 1
                    beta1, beta2 = crossover(alpha1,alpha2)

                    alpha[i][j] = beta1 if random.random() > 0.5 else beta2

                    k[i][j] = 1
            elif a[i][j] == 1:
                contaAlpha += 1
                ageYouth, ageMaturiN, ageOld = p(alpha[i][j])
                age = ageYouth + ageMaturiN + ageOld
                if pk(alpha[i][j],k[i][j]) == age:
                    k[i][j] = 0
                    a[i][j] = 0
                else:
                    k[i][j] = pk(alpha[i][j],k[i][j])
                    k[i][j] += 1
            elif a[i][j] == -1:
                move_seeds(i, j)

    if aux_cont % plaguePeriod == 0:
        gera_seeds(contaAlpha)

    return a, contaAlpha

# --------------------------------------------- #
def find_two_mature_neighbors(i, j):
    alpha1 = []
    alpha2 = []
    numVizinhos = 0

    ageYouth, ageMaturiN, ageOld = p(alpha[i][j-1])
    if a[i][j-1] == 1 and numVizinhos < 2 and k[i][j-1] > ageYouth and k[i][j-1] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i][j-1]
        else:
            alpha2 = alpha[i][j-1]

    ageYouth, ageMaturiN, ageOld = p(alpha[i+1][j-1])
    if a[i+1][j-1] == 1 and numVizinhos < 2 and k[i+1][j-1] > ageYouth and k[i+1][j-1] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i+1][j-1]
        else:
            alpha2 = alpha[i+1][j-1]


    ageYouth, ageMaturiN, ageOld = p(alpha[i+1][j])
    if a[i+1][j] == 1 and numVizinhos < 2 and k[i+1][j] > ageYouth and k[i+1][j] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i+1][j]
        else:
            alpha2 = alpha[i+1][j]

    ageYouth, ageMaturiN, ageOld = p(alpha[i+1][j+1])
    if a[i+1][j+1] == 1 and numVizinhos < 2 and k[i+1][j+1] > ageYouth and k[i+1][j+1] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i+1][j+1]
        else:
            alpha2 = alpha[i+1][j+1]

    ageYouth, ageMaturiN, ageOld = p(alpha[i][j+1])
    if a[i][j+1] == 1 and numVizinhos < 2 and k[i][j+1] > ageYouth and k[i][j+1] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i][j+1]
        else:
            alpha2 = alpha[i][j+1]

    ageYouth, ageMaturiN, ageOld = p(alpha[i-1][j+1])
    if a[i-1][j+1] == 1 and numVizinhos < 2 and k[i-1][j+1] > ageYouth and k[i-1][j+1] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i-1][j+1]
        else:   
            alpha2 = alpha[i-1][j+1]


    ageYouth, ageMaturiN, ageOld = p(alpha[i-1][j])
    if a[i-1][j] == 1 and numVizinhos < 2 and k[i-1][j] > ageYouth and k[i-1][j] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i-1][j]
        else:
            alpha2 = alpha[i-1][j]


    ageYouth, ageMaturiN, ageOld = p(alpha[i-1][j-1])
    if a[i-1][j-1] == 1 and numVizinhos < 2 and k[i-1][j-1] > ageYouth and k[i-1][j-1] <= (ageYouth + ageMaturiN):
        numVizinhos += 1
        if alpha1 == []:
            alpha1 = alpha[i-1][j-1]
        else:
            alpha2 = alpha[i-1][j-1]

    if numVizinhos == 2:
        return alpha1, alpha2, numVizinhos
    else:
        return alpha1, alpha2, numVizinhos

# --------------------------------------------- #
def p(_alpha):
    age = 0
    youth1 = 0
    maturiN1 = 0
    old1 = 0

    for x1 in range(3):
        if x1 == 0:
            for y1 in range(l):
                if _alpha[x1][y1] == 1:
                    youth1 += 1
        if x1 == 1:
            for y1 in range(m):
                if _alpha[x1][y1] == 1:
                    maturiN1 += 1
        if x1 == 2:
            for y1 in range(n):
                if _alpha[x1][y1] == 1:
                    old1 += 1

    age = youth1, maturiN1, old1
            
#    for col in range(L):
#        if alpha[col] == 1:
#            age += 1

    return age

# --------------------------------------------- #
def pk(alpha, k):
    ageYouth, ageMaturiN, ageOld = p(alpha)

    if (ageYouth + ageMaturiN + ageOld) >= k:
        return k
    else:
        return (ageYouth + ageMaturiN + ageOld)


def crossover(alpha1, alpha2):
    beta1 = alpha1
    beta2 = alpha2

    beta1[0] = alpha1[0][:(l/2)] + alpha2[0][(l/2):]
    beta2[0] = alpha2[0][:(l/2)] + alpha1[0][(l/2):]
    beta1[1] = alpha1[1][:(m/2)] + alpha2[1][(m/2):]
    beta2[1] = alpha2[1][:(m/2)] + alpha1[1][(m/2):]
    beta1[2] = alpha1[2][:(n/2)] + alpha2[2][(n/2):]
    beta2[2] = alpha2[2][:(n/2)] + alpha1[2][(n/2):]

    return beta1, beta2


# --------------------------------------------- #
def gera_populacao(p0):
    x = 0
    while x < int(N*N*p0):
        i1 = random.randint(0,N-1)
        j1 = random.randint(0,N-1)

        randBinList = lambda n: [random.randint(0,1) for b in range(1,n+1)]
        
        if a[i1][j1] == 1:
            x -= 1

        a[i1][j1] = 1
        k[i1][j1] = 1

        alpha[i1][j1][0] = randBinList(l)

        alpha[i1][j1][1] = randBinList(m)

        alpha[i1][j1][2] = randBinList(n)
        
        x += 1

    return x

# --------------------------------------------- #
def gera_seeds(populacao):
    x = 0
    while x < int(populacao*dose):
        i1 = random.randint(0,N-1)
        j1 = random.randint(0,N-1)
        
        if a[i1][j1] == -1:
            x -= 1

        if a[i1][j1] == 1:
            a[i1][j1] = 0
            k[i1][j1] = 0
        else:
            a[i1][j1] = -1
            k[i1][j1] = 0

        x += 1


    return

def move_seeds(i, j):
    selecionado = random.randint(0,7)

    if selecionado == 0:
        if a[i][j-1] == 1:
            a[i][j-1] = 0
            k[i][j-1] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i][j-1] == 0:
            a[i][j-1] = -1
            k[i][j-1] = 0
            a[i][j] = 0
            k[i][j] = 0
        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 1:
        if a[i+1][j-1] == 1:
            a[i+1][j-1] = 0
            k[i+1][j-1] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i+1][j-1] == 0:
            a[i+1][j-1] = -1
            k[i+1][j-1] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 2:
        if a[i+1][j] == 1:
            a[i+1][j] = 0
            k[i+1][j] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i+1][j] == 0:
            a[i+1][j] = -1
            k[i+1][j] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 3:
        if a[i+1][j+1] == 1:
            a[i+1][j+1] = 0
            k[i+1][j+1] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i+1][j+1] == 0:
            a[i+1][j+1] = -1
            k[i+1][j+1] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 4:
        if a[i][j+1] == 1:
            a[i][j+1] = 0
            k[i][j+1] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i][j+1] == 0:
            a[i][j+1] = -1
            k[i][j+1] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 5:
        if a[i-1][j+1] == 1:
            a[i-1][j+1] = 0
            k[i-1][j+1] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i-1][j+1] == 0:
            a[i-1][j+1] = -1
            k[i-1][j+1] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 6:
        if a[i-1][j] == 1:
            a[i-1][j] = 0
            k[i-1][j] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i-1][j] == 0:
            a[i-1][j] = -1
            k[i-1][j] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0


    elif selecionado == 7:
        if a[i-1][j-1] == 1:
            a[i-1][j-1] = 0
            k[i-1][j-1] = 0
            a[i][j] = 0
            k[i][j] = 0
        elif a[i-1][j-1] == 0:
            a[i-1][j-1] = -1
            k[i-1][j-1] = 0
            a[i][j] = 0
            k[i][j] = 0

        else:
			a[i][j] = -1
			k[i][j] = 0



    return


#   MAIN   #
if __name__ == "__main__":
    global stop
    aux_cont = 0
    populacao = 0
    stop = 0
    def_var()

### CONFIGURAÇÃO DO SISTEMA ###
    MAX = 2000
    p0 = 0.45           # Initial densiN (P0)
    N = 100              # Lattice size (N×N)
    l = 32              # Youth   - length
    m = 32              # Mature  - length
    n = 32              # Old age - length
    dose = 0.4          # quantidade de praga
    plaguePeriod = 50   # Periodo da praga
### ----------------------- ###

    # INICIALIZA VARIAVEIS
    ## Variaveis de configuração do automato celular
    L = l+m+n
    youth = [0 for col in range(l)]
    maturit = [0 for col in range(m)]
    old = [0 for col in range(n)]
    alpha = [[[youth, maturit, old] for row in range(-1,N+1)] for col in range(-1,N+1)]
    a = [[0 for row in range(-1,N+1)] for col in range(-1,N+1)]
    k = [[0 for row in range(-1,N+1)] for col in range(-1,N+1)]

    populacao = gera_populacao(p0)

    ## Variaveis para desenho no ecran
    XY = 5
    root = Tk()
    root.title("Simulador do Ambiênte")

    canvas  = Canvas(root, width=N*XY, height=N*XY, highlightthickness=0, bd=0, bg='white')
    cell = [[0 for row in range(-1,N+1)] for col in range(-1,N+1)]

    for j in range(N):
        for i in range(N):
            cell[i][j] = canvas.create_oval((i*XY, j*XY, i*XY+XY, j*XY+XY),outline="gray",fill="white")

    monta(a, aux_cont)

    root.mainloop()


