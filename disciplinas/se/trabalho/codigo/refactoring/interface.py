from Tkinter import *

import cellular_automata

class Interface(Frame):

    def __init__(self, parent):
        Frame.__init__(self, parent, background="white")
        
        self.parent = parent
        
        self.points = 5        
        self.pause = True
        self.t = 0
        self.ca = cellular_automata.CellularAutomata()
        
        self.initUI()
    
    def initUI(self):
    
        self.parent.title("Cellular Automata")

        toolbar = Frame(self.parent, bd=1, relief=RAISED)

        button1 = Button(
            toolbar, text='Play', 
            command=lambda b='play': self.serviceToolbar(b)
        )
        button2 = Button(
            toolbar, text='Pause', 
            command=lambda b='pause': self.serviceToolbar(b)
        )
        button3 = Button(
            toolbar, text='Stop', 
            command=lambda b='stop': self.serviceToolbar(b)
        )
        button4 = Button(
            toolbar, text='Next', 
            command=lambda b='next': self.serviceToolbar(b)
        )

        button3.pack(side=LEFT, padx=2, pady=2)
        button1.pack(side=LEFT, padx=2, pady=2)
        button2.pack(side=LEFT, padx=2, pady=2)
        button4.pack(side=LEFT, padx=2, pady=2)

        exitButton = Button(toolbar, text='Quit', command=self.quit)
        exitButton.pack(side=RIGHT, padx=2, pady=2)

        self.canvas  = Canvas(
            self.parent,
            width=self.ca.lattice_size*self.points,
            height=self.ca.lattice_size*self.points,
            highlightthickness=0, 
            bd=0,
            bg='white'
        )

        self.cell = [
            [0 for y in range(self.ca.lattice_size)] 
               for x in range(self.ca.lattice_size)]

        for i in range(self.ca.lattice_size):
            for j in range(self.ca.lattice_size):
                self.cell[i][j] = self.canvas.create_rectangle(
                    (i*self.points, j*self.points, i*self.points+self.points, j*self.points+self.points),outline="gray",fill="white"
                )

        toolbar.pack(side=TOP, fill=X)
        self.canvas.pack(side=TOP)
        self.pack()

    def serviceToolbar(self, button):
        if button == 'play':
            self.pause = False
            self.onPlay()
        if button == 'pause':
            self.pause = True
        if button == 'stop':
            self.pause = True
            self.t = 0
        if button == 'next':
            self.pause = True
            self.onNext()

    def onExit(self):
        self.quit()

    def onPlay(self):
        if not self.pause:
            if self.t == 0:        
                self.ca.generate_population()
            else:
                self.ca.evolution()
                self.ca.move_individuals()

            self.drawing()
           
            self.t += 1
            
            print ('Iteracao', self.t, 
                'Population', self.ca.population,
                'youth', self.ca.population_youth,
                'mature', self.ca.population_mature,
                'old', self.ca.population_old
            )
            
            self.after(10, self.onPlay)


    def onNext(self):

        if self.t == 0:        
            self.ca.generate_population()
        else:
            self.ca.evolution()
            self.ca.move_individuals()
        self.drawing()
       
        self.t += 1
        
        print ('Iteracao', self.t, 
            'Population', self.ca.population,
            'youth', self.ca.population_youth,
            'mature', self.ca.population_mature,
            'old', self.ca.population_old
        )

    def drawing(self):

        population_youth = 0
        population_mature = 0
        population_old = 0
        population = 0
    
        for i in range(self.ca.lattice_size):
            for j in range(self.ca.lattice_size):

                if self.ca.lattice[i][j] == 1:
                    population += 1
                    youth, mature, old = self.ca.number_ones_genetic_code(self.ca.genetic_code[i][j]) 
                    if self.ca.life_time[i][j] <= youth:
                        population_youth += 1
                        self.canvas.itemconfig(self.cell[j][i], fill="yellow")
                    elif self.ca.life_time[i][j] > youth and self.ca.life_time[i][j] <= (youth+mature):
                        population_mature += 1
                        self.canvas.itemconfig(self.cell[j][i], fill="red")
                    else:
                        population_old += 1
                        self.canvas.itemconfig(self.cell[j][i], fill="blue")
                elif self.ca.lattice[i][j] == 0:
                    self.canvas.itemconfig(self.cell[j][i], fill="white")
                    
        self.ca.population_youth = population_youth
        self.ca.population_mature = population_mature
        self.ca.population_old = population_old
        self.ca.population = population


def main():
  
    root = Tk()
    app = Interface(root)    
    root.mainloop()  


if __name__ == '__main__':
    main()  
