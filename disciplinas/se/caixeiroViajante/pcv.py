#!/usr/bin/python
# -*- coding: latin-1 -*-
import os, sys

<<<<<<< HEAD
# Módulo de contrução da matriz do caixeiro viajante
# Autor: Alexandre Gomes da Costa
# Disciplina: Sistemas Evolutivo 2013/2

## Construindo a matriz
def inicializaMatriz():
	## Populando matriz
=======
# Módulo para gerar a matriz de contrução do Problema do Caixeiro Viajante
# Autor: Alexandre Gomes da Costa
# Disciplina: Sistemas Evolutivo 2013/2

def inicializaMatriz():
>>>>>>> a740a8ec7334bbab4b982d9980461daeccceb434
	c1 = [0,2,1,4,9,1]
	c2 = [2,0,5,9,7,2]
	c3 = [1,5,0,3,8,6]
	c4 = [4,9,3,0,2,5]
	c5 = [9,7,8,2,0,2]
	c6 = [1,2,6,5,2,0]
<<<<<<< HEAD
	## Agregando rotas
	matriz = [c1,c2,c3,c4,c5,c6]

	return matriz
=======

	return [c1,c2,c3,c4,c5,c6]
>>>>>>> a740a8ec7334bbab4b982d9980461daeccceb434

