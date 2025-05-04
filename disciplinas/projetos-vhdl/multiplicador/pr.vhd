library ieee;
use ieee.std_logic_1164.all;

entity pr is
	port (
		d: in std_logic_vector(4 downto 0);
		limpa, desloca: in std_logic;
		q_somador: out std_logic_vector(3 downto 0);
		q: out std_logic_vector(7 downto 0)
	);
end pr;

architecture behavior of pr is
	signal d_tmp: std_logic_vector(7 downto 0);
begin
	process(limpa, desloca)
	begin
		if limpa = '1' then
			d_tmp <= "00000000";
		elsif (desloca'event and desloca = '1') then
			d_tmp(6 downto 0) <= d_tmp(7 downto 1);
			d_tmp(7 downto 3) <= d;
		end if;
	end process;
	
	q_somador <= d_tmp(7 downto 4);
	q <= d_tmp;
end behavior;