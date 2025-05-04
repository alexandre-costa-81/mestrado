library ieee;
use ieee.std_logic_1164.all;

entity md is
	port (
		d: in std_logic_vector(3 downto 0);
		carga: in std_logic;
		q: out std_logic_vector(3 downto 0)
	);
end md;
architecture behavior of md is
signal d_tmp: std_logic_vector(3 downto 0);
begin

	process(carga)
	begin
		if (carga'event and carga = '1') then
			d_tmp <= d;
		end if;
	end process;

	q <= d_tmp;

end behavior ;