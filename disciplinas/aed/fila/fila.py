#!/usr/bin/env python
__author__      = "Alexandre Costa"
__copyright__   = "Copyright 2014, Projeto AED"
__credits__     = ["Alexandre Costa"]
__license__     = "GPL"
__version__     = "1.0"
__maintainer__  = "Alexandre Costa"
__email__       = "alexandre.costa@inf.ufpel.edu.br"
__status__      = "Desenvolvimento"

class No:
  def __init__(self, info=None, link=None):
    self.info = info
    self.link = link

class Fila:
    def __init__(self):
        self.tamanho = 0
        self.frente = None
        self.traz = None

    def insereFila(self, info):
        no = No(info)
        if (self.traz!=None):
            self.traz.link = no
            self.traz = no
        else:
            self.traz = no
            self.frente = no

    def removerFila(self):
        if (self.frente==None):
            return None
        else:
            removendo = self.frente
            self.frente = removendo.link
            if (self.frente==None):
                self.traz = None
            return removendo.info

# Funcao principal
def main():
    fila = Fila()

    fila.insereFila(10)
    fila.insereFila(20)
    fila.insereFila(30)
    
    imprime = fila.frente

    while (fila.frente!=None):
        print fila.removerFila()

    return

if __name__ == "__main__":
    main()
