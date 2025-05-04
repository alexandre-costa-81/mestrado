library IEEE;
use IEEE.numeric_std.all;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_arith.all;
use IEEE.std_logic_unsigned.all;

entity pc is
	port (
		d: in std_logic_vector(7 downto 0);
		reset, carga: in std_logic;
		q: out std_logic_vector (7 downto 0)
	);
end pc;

architecture behavior of pc is
signal count: std_logic_vector (7 downto 0);
signal um: std_logic_vector (7 downto 0);
begin
	process (reset, carga)
	begin
		um <= "00000001";
		if reset = '1' then
			count <= "00000000";
		else
			if carga = '1' then
				count <= count;
			else
				count <= count + um;
			end if ;
		end if ;
	end process ;
	q <= count ;
end behavior ;