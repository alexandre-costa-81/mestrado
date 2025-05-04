library ieee;
use ieee.std_logic_1164.all;

entity multplexador2x1 is
	port (
		sel: in  std_logic;
		entrada1: in  std_logic_vector (3 downto 0);
		saida: out std_logic_vector (3 downto 0)
	);
end multplexador2x1;

architecture behavioral of multplexador2x1 is
begin
	saida <= "0000" when sel = '0' else entrada1;
end behavioral;