library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_arith.all;
use IEEE.std_logic_unsigned.all;

entity controle is
	port(
		clk       : in std_logic;
		reset     : in std_logic;
		entrada   : in std_logic;

		limpa_pr: out std_logic;

		carga_md: out std_logic;
		carga_mr: out std_logic;

		desloca_mr: out std_logic;
		desloca_pr: out std_logic
	);
end controle;

architecture arq of controle is
	type STATE_TYPE is (inicial, aguarda, somar, deslocar, resultado);
	signal estado_atual, proximo_estado: STATE_TYPE;
	signal contador_clk, contador_limpa, contador_mr: std_logic;
	
begin
	process (reset, clk)
	begin
		if (reset = '0') then
			estado_atual <= inicial;
		elsif (clk'EVENT and clk = '1') then
			estado_atual <= proximo_estado;
		end if;
	end process;

	process (estado_atual, entrada)
	variable limpa_pr_tmp, carga_md_tmp, carga_mr_tmp, desloca_mr_tmp, desloca_pr_tmp: std_logic;
	begin
		case estado_atual is
			when inicial =>
				limpa_pr_tmp := '0';
				carga_md_tmp := '0';
				carga_mr_tmp := '0';
				desloca_mr_tmp := '0';
				desloca_pr_tmp := '0';
			
				contador_clk <= '0';
				contador_limpa <= '1';
			
				proximo_estado <= aguarda;
					
			when aguarda =>
				if (entrada = '1') then
					limpa_pr_tmp := '1';
					carga_md_tmp := '1';
					carga_mr_tmp := '1';
					desloca_mr_tmp := '0';
					desloca_pr_tmp := '0';
					
					contador_clk <= '0';
					contador_limpa <= '0';
					
					proximo_estado <= somar;
				else
					proximo_estado <= aguarda;
				end if;

			when somar =>
				if (contador_mr = '0') then
								limpa_pr_tmp := '0';
				carga_md_tmp := '0';
				carga_mr_tmp := '0';
				desloca_mr_tmp := '0';
				desloca_pr_tmp := '0';

				contador_clk <= '0';
				contador_limpa <= '0';
					proximo_estado <= deslocar;
				else
					proximo_estado <= resultado;
				end if;

	
			
			when deslocar =>
				limpa_pr_tmp := '0';
				carga_md_tmp := '0';
				carga_mr_tmp := '0';
				desloca_mr_tmp := '1';
				desloca_pr_tmp := '1';
				
				contador_clk <= '1';
				contador_limpa <= '0';
				
				proximo_estado <= somar;
			
			when resultado =>
				limpa_pr_tmp := '0';
				carga_md_tmp := '0';
				carga_mr_tmp := '0';
				desloca_mr_tmp := '0';
				desloca_pr_tmp := '0';

				contador_clk <= '0';
				contador_limpa <= '0';

				proximo_estado <= resultado;
		end case;

		limpa_pr <= limpa_pr_tmp;
		carga_md <= carga_md_tmp;
		carga_mr <= carga_mr_tmp;
		desloca_mr <= desloca_mr_tmp;
		desloca_pr <= desloca_pr_tmp;
	end process;

	process (contador_clk, contador_limpa)
	variable contador: integer range 0 to 7;
	begin
		if (contador_limpa = '1') then
			contador:= 0;
			contador_mr <= '0';
		elsif (contador_clk'event and contador_clk = '1') then
			if (contador < 3) then
				contador := contador + 1;
			else
				contador := 0;
				contador_mr <= '1';
			end if;
		end if;
	end process;
	
end arq;
