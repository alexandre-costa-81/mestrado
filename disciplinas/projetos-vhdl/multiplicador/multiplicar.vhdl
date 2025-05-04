library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_arith.all;
use IEEE.std_logic_unsigned.all;

entity multiplicar is
	port (
		md: in std_logic_vector(3 downto 0);
		mr: in std_logic_vector(3 downto 0);
		pr: out std_logic_vector(7 downto 0)
	);
end multiplicar;

architecture arq of multiplicar is
begin
	pr <= md * mr;
end arq;