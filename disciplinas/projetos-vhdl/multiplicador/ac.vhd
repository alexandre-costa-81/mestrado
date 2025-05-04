-- AC: ACUMULADOR
-- Registrador paralelo de 8 bits com sinal de carga
-- Autor: Alexandre

library IEEE;
use IEEE.std_logic_1164.all;

entity ac is
	port (
		d: in std_logic_vector(7 downto 0);
		reset, carga: in std_logic;
		q: out std_logic_vector(7 downto 0)
	);
end ac;

architecture behavior of ac is
signal d_tmp: std_logic_vector(7 downto 0);
begin
	process(reset, carga)
	begin
		if (reset = '0') then
			d_tmp <= "00000000";
		else
			if (carga'event and carga = '1') then
				d_tmp <= d;
			else
				d_tmp <= d_tmp + 1;
			end if;
		end if;
	end process;
	q <= d_tmp;
end behavior ;