# -*- coding: utf-8 -*-

"""
Created: 15/01/2014
@autor: Alexandre Costa
@disciplina: Sistemas Evolutivos
@professor: Marilton Aguiar
@descrição: trabalho final da @disciplina, onde foi desenvolvido um Algoritmo de
 Busca Tabu que simula o ataque de pragas periódicas a um conjunto de colonias.
"""

import random

class AutomatoCelular:
    def __init__(self):
        self.maximo = 2000
        self.lattice_size = 100
        self.initial_density = 0.02
        self.youth_length = 32
        self.mature_length = 32
        self.old_length = 32
        self.dose = 0.4
        self.plague_period = 50
        
        self.population = 0

        self.generation = 0

        self.lattice = [
            [0 for y in range(self.lattice_size)]
               for x in range(self.lattice_size)]

        self.lattice_next = [
            [0 for y in range(self.lattice_size)]
               for x in range(self.lattice_size)]

        self.life_time = [
            [0 for y in range(self.lattice_size)]
               for x in range(self.lattice_size)]

        self.life_time_next = [
            [0 for y in range(self.lattice_size)]
               for x in range(self.lattice_size)]

        self.youth = [0 for x in range(self.youth_length)]
        self.mature = [0 for x in range(self.mature_length)]
        self.old = [0 for x in range(self.old_length)]

        self.genetic_code = [
            [[self.youth, self.mature, self.old]
            for y in range(self.lattice_size)]
            for x in range(self.lattice_size)]

        self.genetic_code_next = [
            [[self.youth, self.mature, self.old]
            for y in range(self.lattice_size)]
            for x in range(self.lattice_size)]
        
        
        
    def generate_population(self, initial_density):
        if initial_density != 0:
            self.initial_density = initial_density 
        
        x = 0
        
        while x < int(self.lattice_size*self.lattice_size*self.initial_density):
            i1 = random.randint(0,self.lattice_size-1)
            j1 = random.randint(0,self.lattice_size-1)

            randBinList = lambda n: [random.randint(0,1) for b in range(1,n+1)]
        
            if self.lattice_next[i1][j1] == 1:
                x -= 1

            self.lattice_next[i1][j1] = 1
            self.life_time_next[i1][j1] = 1

            self.genetic_code_next[i1][j1][0] = randBinList(self.youth_length)
            self.genetic_code_next[i1][j1][1] = randBinList(self.mature_length)
            self.genetic_code_next[i1][j1][2] = randBinList(self.old_length)
        
            x += 1

        return x, self.lattice_next, self.life_time_next, self.genetic_code_next
        
def main():
    
    ac = AutomatoCelular()
    
    ac.population, ac.lattice, ac.life_time, ac.genetic_code = ac.generate_population(0)
    
    print ac.lattice

if __name__ == "__main__":
    main()

        
