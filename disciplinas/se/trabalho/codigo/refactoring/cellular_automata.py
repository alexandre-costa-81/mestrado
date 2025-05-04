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
from copy import deepcopy

class CellularAutomata:
    maximo = None
    lattice_size = None
    initial_density = None
    youth_length = None
    mature_length = None
    old_length = None
    dose = None
    plague_period = None

    population = None
    population_youth = None
    population_mature = None
    population_old = None

    generation = None
    
    lattice = []
    life_time = []
       
    youth = []
    mature = []
    old = []
    
    genetic_code = []


    def __init__(self, 
            maximo=2000, 
            lattice_size=100, 
            initial_density=0.02, 
            youth_length=32,
            mature_length=32,
            old_length=32,
            dose=0.4,
            plague_period=50
    ):
        self.maximo = maximo
        self.lattice_size = lattice_size
        self.initial_density = initial_density
        self.youth_length = youth_length
        self.mature_length = mature_length
        self.old_length = old_length
        self.dose = dose
        self.plague_period = plague_period
        
        self.population = 0
        self.population_youth = 0
        self.population_mature = 0
        self.population_old = 0

        self.generation = 0

        self.lattice = [
            [0 for y in range(self.lattice_size)]
               for x in range(self.lattice_size)]

        self.life_time = [
            [0 for y in range(self.lattice_size)]
               for x in range(self.lattice_size)]

        self.youth = [0 for x in range(self.youth_length)]
        self.mature = [0 for x in range(self.mature_length)]
        self.old = [0 for x in range(self.old_length)]

        self.genetic_code = [
            [[self.youth, self.mature, self.old]
            for y in range(self.lattice_size)]
            for x in range(self.lattice_size)]

        
    def generate_population(self, initial_density=0):
        lattice_next = deepcopy(self.lattice)
        life_time_next = deepcopy(self.life_time)
        genetic_code_next = deepcopy(self.genetic_code)
        
        if initial_density != 0:
            self.initial_density = initial_density 
        
        x = 0

        while x < int(self.lattice_size*self.lattice_size*self.initial_density):
            i1 = random.randint(0,self.lattice_size-1)
            j1 = random.randint(0,self.lattice_size-1)

            randBinList = lambda n: [random.randint(0,1) for b in range(1,n+1)]
        
            if lattice_next[i1][j1] == 1:
                x -= 1
            else:
                lattice_next[i1][j1] = 1
                life_time_next[i1][j1] = 1

                genetic_code_next[i1][j1][0] = randBinList(self.youth_length)
                genetic_code_next[i1][j1][1] = randBinList(self.mature_length)
                genetic_code_next[i1][j1][2] = randBinList(self.old_length)
        
            x += 1

        self.population = x
        self.lattice = lattice_next
        self.life_time = life_time_next
        self.genetic_code = genetic_code_next


    def number_ones_genetic_code(self, alpha):
        age = 0
        youth = 0
        mature = 0
        old = 0

        for x in range(3):
            if x == 0:
                for y in range(self.youth_length):
                    if alpha[x][y] == 1:
                        youth += 1
            if x == 1:
                for y in range(self.mature_length):
                    if alpha[x][y] == 1:
                        mature += 1
            if x == 2:
                for y in range(self.old_length):
                    if alpha[x][y] == 1:
                        old += 1

        age = youth, mature, old
        
        return age

    def move_individuals(self):

        lattice_next = deepcopy(self.lattice)
        life_time_next = deepcopy(self.life_time)
        genetic_code_next = deepcopy(self.genetic_code)

        for i in range(self.lattice_size):
            for j in range(self.lattice_size):
                if self.lattice[i][j] == 1:
                    
                    selecionado = random.randint(0,7)
                    
                    if selecionado == 0 and j > 0:
                        if self.lattice[i][j-1] == 0 and lattice_next[i][j-1] == 0:
                            lattice_next[i][j-1] = self.lattice[i][j]
                            life_time_next[i][j-1] = self.life_time[i][j]
                            genetic_code_next[i][j-1] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
                    if selecionado == 1 and j > 0 and i < (self.lattice_size-1):
                        if self.lattice[i+1][j-1] == 0 and lattice_next[i+1][j-1] == 0:
                            lattice_next[i+1][j-1] = self.lattice[i][j]
                            life_time_next[i+1][j-1] = self.life_time[i][j]
                            genetic_code_next[i+1][j-1] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                        
                    if selecionado == 2 and i < (self.lattice_size-1):
                        if self.lattice[i+1][j] == 0 and lattice_next[i+1][j] == 0:
                            lattice_next[i+1][j] = self.lattice[i][j]
                            life_time_next[i+1][j] = self.life_time[i][j]
                            genetic_code_next[i+1][j] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
                    if selecionado == 3 and i < (self.lattice_size-1) and j < (self.lattice_size-1):
                        if self.lattice[i+1][j+1] == 0 and lattice_next[i+1][j+1] == 0:
                            lattice_next[i+1][j+1] = self.lattice[i][j]
                            life_time_next[i+1][j+1] = self.life_time[i][j]
                            genetic_code_next[i+1][j+1] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
                    if selecionado == 4 and j < (self.lattice_size-1):
                        if self.lattice[i][j+1] == 0 and lattice_next[i][j+1] == 0:
                            lattice_next[i][j+1] = self.lattice[i][j]
                            life_time_next[i][j+1] = self.life_time[i][j]
                            genetic_code_next[i][j+1] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
                    if selecionado == 5 and j < (self.lattice_size-1) and i > 0:
                        if self.lattice[i-1][j+1] == 0 and lattice_next[i-1][j+1] == 0:
                            lattice_next[i-1][j+1] = self.lattice[i][j]
                            life_time_next[i-1][j+1] = self.life_time[i][j]
                            genetic_code_next[i-1][j+1] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
                    if selecionado == 6 and i > 0:
                        if self.lattice[i-1][j] == 0 and lattice_next[i-1][j] == 0: 
                            lattice_next[i-1][j] = self.lattice[i][j]
                            life_time_next[i-1][j] = self.life_time[i][j]
                            genetic_code_next[i-1][j] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
                    if selecionado == 7 and i > 0 and j > 0:
                        if self.lattice[i-1][j-1] == 0 and lattice_next[i-1][j-1] == 0:
                            lattice_next[i-1][j-1] = self.lattice[i][j]
                            life_time_next[i-1][j-1] = self.life_time[i][j]
                            genetic_code_next[i-1][j-1] = self.genetic_code[i][j]
                            lattice_next[i][j] = 0
                            life_time_next[i][j] = 0
                            genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    
        self.lattice = deepcopy(lattice_next)
        self.life_time = deepcopy(life_time_next)
        self.genetic_code = deepcopy(genetic_code_next)


    def find_two_mature_neighbors(self, i, j):
        alpha1 = []
        alpha2 = []
        neighbors = 0
        
        if j > 0:
            if self.lattice[i][j-1] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i][j-1])
                if self.life_time[i][j-1] > youth and self.life_time[i][j-1] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i][j-1]
                    else:
                        alpha2 = self.genetic_code[i][j-1]
                
        if i < (self.lattice_size-1) and j > 0:
            if self.lattice[i+1][j-1] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i+1][j-1])
                if self.life_time[i+1][j-1] > youth and self.life_time[i+1][j-1] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i+1][j-1]
                    else:
                        alpha2 = self.genetic_code[i+1][j-1]
        
        if i < (self.lattice_size-1):
            if self.lattice[i+1][j] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i+1][j])
                if self.life_time[i+1][j] > youth and self.life_time[i+1][j] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i+1][j]
                    else:
                        alpha2 = self.genetic_code[i+1][j]
                    
        if i < (self.lattice_size-1) and j < (self.lattice_size-1):
            if self.lattice[i+1][j+1] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i+1][j+1])
                if self.life_time[i+1][j+1] > youth and self.life_time[i+1][j+1] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i+1][j+1]
                    else:
                        alpha2 = self.genetic_code[i+1][j+1]
        
        if j < (self.lattice_size-1):
            if self.lattice[i][j+1] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i][j+1])
                if self.life_time[i][j+1] > youth and self.life_time[i][j+1] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i][j+1]
                    else:
                        alpha2 = self.genetic_code[i][j+1]

        if i > 0 and j < (self.lattice_size-1):
            if self.lattice[i-1][j+1] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i-1][j+1])
                if self.life_time[i-1][j+1] > youth and self.life_time[i-1][j+1] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i-1][j+1]
                    else:
                        alpha2 = self.genetic_code[i-1][j+1]

        if i > 0:
            if self.lattice[i-1][j] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i-1][j])
                if self.life_time[i-1][j] > youth and self.life_time[i-1][j] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i-1][j]
                    else:
                        alpha2 = self.genetic_code[i-1][j]

        if i > 0 and j > 0:
            if self.lattice[i-1][j-1] == 1 and neighbors < 2:
                youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i-1][j-1])
                if self.life_time[i-1][j-1] > youth and self.life_time[i-1][j-1] <= (youth+mature):
                    neighbors += 1
                    if alpha1 == []:
                        alpha1 = self.genetic_code[i-1][j-1]
                    else:
                        alpha2 = self.genetic_code[i-1][j-1]
        
        return alpha1, alpha2, neighbors

    def pk(self, alpha, k):
        youth, mature, old = self.number_ones_genetic_code(alpha)

        if (youth+mature+old) >= k:
            return k
        else:
            return (youth+mature+old)

    def evolution(self):
        
        lattice_next = deepcopy(self.lattice)
        life_time_next = deepcopy(self.life_time)
        genetic_code_next = deepcopy(self.genetic_code)
        
        for i in range(self.lattice_size):
            for j in range(self.lattice_size):
                
                if self.lattice[i][j] == 0:
                    alpha1, alpha2, neighbors = self.find_two_mature_neighbors(i, j)
                    if neighbors == 2:
                        lattice_next[i][j] = 1
                        life_time_next[i][j] = 1
                        genetic_code_next[i][j] = alpha1 if random.random() > 0.5 else alpha2
                
                elif self.lattice[i][j] == 1:
                    youth, mature, old = self.number_ones_genetic_code(self.genetic_code[i][j])
                    age = (youth+mature+old)
                    if self.pk(self.genetic_code[i][j], self.life_time[i][j]) == age:
                        lattice_next[i][j] = 0
                        life_time_next[i][j] = 0
                        genetic_code_next[i][j] = [self.youth, self.mature, self.old]
                    else:
                        life_time_next[i][j] = self.pk(self.genetic_code[i][j], self.life_time[i][j])
                        life_time_next[i][j] += 1
                        
                    
        self.lattice = deepcopy(lattice_next)
        self.life_time = deepcopy(life_time_next)
        self.genetic_code = deepcopy(genetic_code_next)


    def printMatrix(self, testMatrix):
        print ' ',
        for i in range(len(testMatrix[1])):  # Make it work with non square matrices.
            print i,
        print
        for i, element in enumerate(testMatrix):
            print i, ' '.join(str(element))


def main():
    
    ca = CellularAutomata()
    
    ca.generate_population()
    
    print ("Population",ca.population)
    
    #ca.printMatrix(ca.lattice)
    ca.evolution()
    ca.move_individuals()
    
    #ca.printMatrix(ca.lattice)
    

if __name__ == "__main__":
    main()

        
