library ieee;
use ieee.std_logic_1164.all;

entity mr is
	port (
		d: in std_logic_vector(3 downto 0);
		carga, desloca: in std_logic;
		q: out std_logic
	);
end mr;

architecture behavior of mr is
	signal d_tmp: std_logic_vector(3 downto 0);
begin
	process(carga, desloca)
	begin
		if carga = '1' then
			d_tmp <= d;
		elsif (desloca'event and desloca = '1') then
			d_tmp(0) <= d_tmp(1);
			d_tmp(1) <= d_tmp(2);
			d_tmp(2) <= d_tmp(3);
			d_tmp(3) <= '0';
		end if;
	end process;
	
	q <= d_tmp(0);
end behavior;