#!/usr/bin/python
# -*- coding: latin-1 -*-
import os, sys

# Contrução Gulosa do Problema da Mochila
# Autor: Alexandre Gomes da Costa
# Disciplina: Sistemas Evolutivo 2013/2


## Estrutura de dados


## Inicializa a matriz
def inicializaCG():
	## Peso
	w = [4,5,7,9,6]
	## Valor
	p = [2,2,3,4,4]
	## Objetos
	j = [w,p]
    ## Capacidade
	b = 23

	return j

## Retira o melhor elemento
def melhor(lista):
	aux = 0
	for i in range(len(lista[0])):
		if lista[1][i] > lista[1][aux]:
			aux = i
		elif lista[1][i] == lista[1][aux]:
			if lista[0][i] < lista[0][aux]:
				aux = i
	return aux



## function ConstrucaoGulosa
def construcaoGulosa():
	s = [[],[]]
	C = inicializaCG()
	print C
	for i in range(len(C[0])):
		m = melhor(C)
		s[0].insert(len(s[0]),C[0].pop(m))
		s[1].insert(len(s[1]),C[1].pop(m))
	print C
	return s


## main
print construcaoGulosa()
