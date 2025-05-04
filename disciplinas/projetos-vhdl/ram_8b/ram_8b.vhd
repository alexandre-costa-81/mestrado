-- TRABLHO DE ARQUITETURA E PROJETO DE COMPUTADORES
-- Memoria ram de 8 bits
-- 

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity ram_8b is
	port (
		clock   : in  std_logic;
		rw      : in  std_logic;
		address : in  std_logic_vector(3 downto 0);
		data_in : in  std_logic_vector(7 downto 0);
		data_out: out std_logic_vector(7 downto 0)
	);
end entity ram_8b;

architecture arc of ram_8b is

	type ram_type is array (0 to (2**address'length)-1) of std_logic_vector(data_in'range);
	signal ram          : ram_type;
	signal read_address : std_logic_vector(address'range);

begin

	process(clock) is
	begin

		if (clock'event and clock = '1') then
			if rw = '1' then
				ram(to_integer(unsigned(address))) <= data_in;
			end if;
			read_address <= address;
		end if;

	end process;

	data_out <= ram(to_integer(unsigned(read_address)));

end architecture arc;