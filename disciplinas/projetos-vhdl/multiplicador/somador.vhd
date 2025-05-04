library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_arith.all;
use IEEE.std_logic_unsigned.all;

entity somador is
	port (
		operando_a: in std_logic_vector(3 downto 0);
		operando_b: in std_logic_vector(3 downto 0);
		soma: out std_logic_vector(4 downto 0)
	);
end somador;

architecture arq of somador is
begin
	process(operando_a, operando_b)
	variable s: std_logic_vector(3 downto 0);
	variable c: std_logic;
	begin
		c := '0';
		
		s(0) := operando_a(0) xor operando_b(0) xor c;
		c := (operando_a(0) and operando_b(0)) or ( (operando_a(0) xor operando_b(0)) and c );
		
		s(1) := operando_a(1) xor operando_b(1) xor c;
		c := (operando_a(1) and operando_b(1)) or ( (operando_a(1) xor operando_b(1)) and c );
		
		s(2) := operando_a(2) xor operando_b(2) xor c;
		c := (operando_a(2) and operando_b(2)) or ( (operando_a(2) xor operando_b(2)) and c );
		
		s(3) := operando_a(3) xor operando_b(3) xor c;
		c := (operando_a(3) and operando_b(3)) or ( (operando_a(3) xor operando_b(3)) and c );
		
		soma <= c&s;
	
	end process;
end arq;