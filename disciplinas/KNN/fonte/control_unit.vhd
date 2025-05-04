library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_arith.all;
use IEEE.std_logic_unsigned.all;
use IEEE.numeric_std.all;

entity control_unit is
   port(
      -- IN
      clock                   : in std_logic;
      reset                   : in std_logic;
      alb                     : in std_logic;
		receive_state				: in integer;

      -- OUT
      address_exp             : out std_logic_vector (11 downto 0);
      address_knt             : out integer;
      address_result          : out std_logic_vector (11 downto 0);
      data                    : out std_logic_vector (31 downto 0);
      data_knt                : out std_logic_vector (31 downto 0);
      wren                    : out std_logic;
      wren_knt                : out std_logic;
      wren_result             : out std_logic;
      st                      : out integer;
      control_acc             : out std_logic;
      reset_acc               : out std_logic;
      control_less_distance   : out std_logic;
      reset_ld_out            : out std_logic;
      end_knn                 : out std_logic;
		reset_from_control      : out std_logic
   );
end control_unit;

architecture arq of control_unit is
-- TYPEs
type state is (initial, load_memory, iterate_lm, acc_reset, arith, iterate_arith, sqrt_state, sqrt_it, acc_load, result_load, iterate_rl, iterate, final);

-- SIGNALs
signal current_state             : state;
signal next_state                : state;
signal counter_clock             : std_logic;
signal counter_memory_clear      : std_logic;
signal counter_accumulate_clear  : std_logic;
signal end_loop                  : std_logic;
signal end_accumulate_loop       : std_logic;
signal control_acc_tmp           : std_logic;
signal control_mem_result_tmp    : std_logic;
signal control_less_distance_tmp : std_logic;
signal example_ok                : std_logic;
signal sqrt_ok                   : std_logic;
signal sqrt_calculing            : std_logic;
signal reset_ld                  : std_logic;
signal less_ok                   : std_logic;
signal examples_completed        : integer;
signal aux_tmp                   : integer;
signal aux_accumulate_tmp        : integer;
signal mod_accumulate_tmp        : integer;
signal result_accumulate_tmp     : integer;
signal less_distance_tmp         : integer;
signal comp_ok                   : std_logic;
signal reset_from_control_tmp    : std_logic;

begin
   process (reset, clock)
   begin
      if (reset = '0') then
         current_state <= initial;
      elsif (clock'EVENT and clock = '1') then
         current_state <= next_state;
      end if;
   end process;

   -- states of state machine
   process (current_state)
   variable address_knt_tmp   : integer;
   variable address_exp_tmp   : std_logic_vector (11 downto 0);
   variable address_result_tmp: std_logic_vector (11 downto 0);
   variable data_tmp          : std_logic_vector (31 downto 0);
   variable data_knt_tmp      : std_logic_vector (31 downto 0);
   variable wren_tmp          : std_logic;
   variable wren_knt_tmp      : std_logic;
   variable wren_result_tmp   : std_logic;

   begin
      case current_state is
         when initial =>
            counter_clock              <= '0';
            counter_memory_clear       <= '1';
            counter_accumulate_clear   <= '1';
            control_less_distance_tmp  <= '1';
            --address_knt_tmp            := "000000000000";
            address_knt_tmp            := 0;
            address_exp_tmp            := "000000000000";
            address_result_tmp         := "000000000000";
            data_tmp                   := "00000000000000000000000000000000";
            data_knt_tmp               := "00000000000000000000000000000000";
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            wren_result_tmp            := '0';
            reset_acc                  <= '1';
            reset_ld                   <= '1';
            st                         <=  0;
            sqrt_calculing             <= '0';
				reset_from_control_tmp     <= '0';
				
				if (receive_state = 2) then
					next_state              <= load_memory;
				else
					next_state					<= initial;
				end if;
				
         when load_memory =>
            counter_clock              <= '1';
            reset_acc                  <= '0';
            counter_memory_clear       <= '0';
            control_less_distance_tmp  <= '0';
            reset_ld                   <= '0';
            --address_knt_tmp          := std_logic_vector(to_unsigned(aux_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_knt_tmp               := '0';
            wren_result_tmp            := '0';
            sqrt_calculing             <= '0';
            wren_tmp                   := '0';
				reset_from_control_tmp     <= '0';
            st                         <= 1;
         if (end_loop = '0') then
            next_state <= iterate_lm;
         else
            next_state <= acc_reset;
         end if;

         when iterate_lm =>
            --address_knt_tmp          := std_logic_vector(to_unsigned(aux_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            counter_clock              <= '0';
            reset_acc                  <= '0';
            reset_ld                   <= '0';
            counter_memory_clear       <= '0';
            control_less_distance_tmp  <= '0';
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            wren_result_tmp            := '0';
            sqrt_calculing             <= '0';
				reset_from_control_tmp     <= '0';
            st                         <= 2;
            next_state                 <= load_memory;

         when acc_reset =>
            --address_knt_tmp          := "000000000000";
            address_knt_tmp            := 0;
            address_exp_tmp            := "000000000000";
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            counter_clock              <= '0';
            reset_acc                  <= '0';
            control_less_distance_tmp  <= '0';
            counter_memory_clear       <= '0';
            counter_accumulate_clear   <= '1';
            wren_result_tmp            := '0';
            sqrt_calculing             <= '0';
				reset_from_control_tmp     <= '0';
            st                         <= 3;
            next_state                 <= iterate_arith;

         when arith =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            reset_acc                  <= '0';
            counter_clock              <= '1';
            counter_memory_clear       <= '0';
            wren_result_tmp            := '0';
            control_less_distance_tmp  <= '0';
            counter_accumulate_clear   <= '0';
            sqrt_calculing             <= '0';
				reset_from_control_tmp     <= '0';
            st                         <= 4;
            if (control_acc_tmp = '0') then
               next_state <= iterate_arith;
            else
               next_state <= acc_load;
            end if;

         when iterate_arith =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            counter_clock              <= '0';
            reset_acc                  <= '0';
            counter_memory_clear       <= '0';
            control_less_distance_tmp  <= '0';
            counter_accumulate_clear   <= '0';
            wren_result_tmp            := '0';
            sqrt_calculing             <= '0';
				reset_from_control_tmp     <= '0';
            st                         <= 5;
            next_state                 <= arith;

         when acc_load =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            sqrt_calculing             <= '0';
            reset_acc                  <= '0';
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            wren_result_tmp            := '0';
            counter_clock              <= '0';
            control_less_distance_tmp  <= '0';
            counter_memory_clear       <= '0';
				reset_from_control_tmp     <= '0';
            counter_accumulate_clear   <= '0';
            st                         <= 6;

            if (example_ok = '0') then
               next_state <= arith;
            else
               next_state <= sqrt_it;
            end if;

         when sqrt_state =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            reset_acc                  <= '0';
            sqrt_calculing             <= '1';
            counter_clock              <= '1';
            counter_memory_clear       <= '0';
            control_less_distance_tmp  <= '0';
            wren_result_tmp            := '0';
            counter_accumulate_clear   <= '0';
				reset_from_control_tmp     <= '0';
            st                         <= 7;

            if (sqrt_ok = '0') then
               next_state <= sqrt_it;
            else
               next_state <= result_load;
            end if;

         when sqrt_it =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            reset_acc                  <= '0';
            reset_ld                   <= '0';
            wren_result_tmp            := '0';
            control_less_distance_tmp  <= '0';
            sqrt_calculing             <= '1';
				reset_from_control_tmp     <= '0';
            counter_clock              <= '0';
            counter_memory_clear       <= '0';
            counter_accumulate_clear   <= '0';
            st                         <= 8;
            next_state                 <= sqrt_state;

         when result_load =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_result_tmp            := '1';
            sqrt_calculing             <= '1';
            reset_acc                  <= '0';
				reset_from_control_tmp     <= '0';
            reset_ld                   <= '0';
            counter_clock              <= '1';
            st                         <= 99;
            counter_accumulate_clear   <= '0';

            if (alb = '1') then
               control_less_distance_tmp <= '1';
            else
               control_less_distance_tmp <= '0';
            end if;

            if (comp_ok = '0') then
               next_state <= iterate_rl;
            else
               next_state <= iterate;
            end if;

         when iterate_rl =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp         := mod_accumulate_tmp;
            address_exp_tmp         := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp      := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_result_tmp         := '1';
            sqrt_calculing          <= '1';
				reset_from_control_tmp  <= '0';
            reset_acc               <= '0';
            reset_ld                <= '0';
            counter_clock           <= '0';
            if (alb = '1') then
               control_less_distance_tmp <= '1';
            else
               control_less_distance_tmp <= '0';
            end if;

            st                      <= 9;
            counter_accumulate_clear<= '0';
            next_state              <= result_load;

         when iterate =>
            --address_knt_tmp         := std_logic_vector(to_unsigned(mod_accumulate_tmp, address_knt_tmp'length));
            address_knt_tmp            := mod_accumulate_tmp;
            address_exp_tmp            := std_logic_vector(to_unsigned(aux_accumulate_tmp, address_exp_tmp'length));
            address_result_tmp         := std_logic_vector(to_unsigned(result_accumulate_tmp, address_result_tmp'length));
            wren_result_tmp            := '0';
            sqrt_calculing             <= '0';
            control_less_distance_tmp  <= '0';
            reset_acc                  <= '1';
				reset_from_control_tmp     <= '0';
            reset_ld                   <= '0';
            st                         <= 11;
            counter_accumulate_clear   <= '0';

            if (end_accumulate_loop = '0') then
               next_state <= arith;
            else
               next_state <= final;
            end if;

         when final =>
            --address_knt_tmp         := "000000000000";
            address_knt_tmp            := 0;
            address_exp_tmp            := "000000000000";
            address_result_tmp         := "000000000000";
            wren_tmp                   := '0';
            wren_knt_tmp               := '0';
            control_less_distance_tmp  <= '0';
            wren_result_tmp            := '0';
            reset_ld                   <= '0';
				reset_from_control_tmp     <= '1';
            counter_memory_clear       <= '0';
            counter_accumulate_clear   <= '0';
            counter_clock              <= '0';
            st                         <= 10;
            next_state                 <= final;
      end case;

      address_knt            <= address_knt_tmp;
      address_exp            <= address_exp_tmp;
      data                   <= data_tmp;
      data_knt               <= data_knt_tmp;
      wren                   <= wren_tmp;
      wren_knt               <= wren_knt_tmp;
      control_acc            <= control_acc_tmp;
      wren_result            <= wren_result_tmp;
      address_result         <= address_result_tmp;
      control_less_distance  <= control_less_distance_tmp;
      reset_ld_out           <= reset_ld;
		reset_from_control     <= reset_from_control_tmp;
   end process;

   -- LOOP -> Counter of memory management
   process (counter_clock, counter_memory_clear)
   variable counter_memory	: integer;
   begin
      if (counter_memory_clear = '1') then
         counter_memory := -1;
         end_loop <= '0';
      elsif (counter_clock'event and counter_clock = '1') then
         if (counter_memory < 18) then
            counter_memory := counter_memory + 1;
         else
            end_loop <= '1';
         end if;
      end if;
      aux_tmp <= counter_memory;	
   end process;

   -- LOOP -> Counter of accumulate state
   process (counter_clock, counter_accumulate_clear)
   variable counter_accumulate            : integer;
   variable counter_accumulate_address    : integer;
   variable counter_less_distance_address : integer;
   variable number_of_data_columns        : integer; -- number of examples * number of columns in each example
   variable number_of_clocks              : integer; -- necessary clocks to finish the sub, mult and add arithmetic operations
   variable sqrt_clocks                   : integer; -- necessary clocks to finish the sqrt operation
   variable number_of_data_knt_columns    : integer;
   variable mod_aux                       : integer;
   variable control_acc_aux               : std_logic;
   variable repeated                      : integer;
   variable sqrt_counter                  : integer;
   variable counter_result_address        : integer;

   begin
      if (counter_accumulate_clear = '1') then
         counter_accumulate            := 0;
         counter_accumulate_address    := 0;
         counter_less_distance_address := 0;
         end_accumulate_loop           <= '0';
         example_ok                    <= '0';
         mod_aux                       := 0;
         control_acc_aux               := '0';
         number_of_data_columns        := 10050;
         number_of_data_knt_columns    := 75;
         number_of_clocks              := 30;
         sqrt_clocks                   := 15;
         sqrt_ok                       <= '0';
         sqrt_counter                  := 0;
         less_ok                       <= '0';
         repeated                      := 1;
         counter_result_address        := 0;
         comp_ok                       <= '0';
      elsif (counter_clock'event and counter_clock = '1') then
         sqrt_ok <= '0';
        
         if (sqrt_calculing = '0') then
            if (counter_accumulate < number_of_clocks * repeated) then
               counter_accumulate   := counter_accumulate + 1;
               control_acc_aux      := '0';
            else
               control_acc_aux            := '1';
               counter_accumulate         := counter_accumulate + 1;
               counter_accumulate_address := counter_accumulate_address + 1;	
               mod_aux                    := counter_accumulate_address mod 75;
               if (mod_aux = 0) then
                  example_ok <= '1';
               end if;

               repeated  := repeated + 1;

               if (counter_accumulate_address = number_of_data_columns * repeated) then
                  end_accumulate_loop <= '1';
               end if;
            end if;
         else					
            control_acc_aux := '0';
            if (sqrt_counter < sqrt_clocks) then
               sqrt_counter := sqrt_counter + 1;
            else
               repeated := 1;
               counter_accumulate := 0;
               sqrt_ok  <= '1';
               example_ok <= '0';

               if (counter_result_address > 5) then
                  comp_ok <= '1';
                  counter_result_address := 0;
               else
                  counter_result_address := counter_result_address + 1;
               end if;
            end if;
         end if;
      end if;

      aux_accumulate_tmp   <= counter_accumulate_address;
      mod_accumulate_tmp   <= mod_aux;
      control_acc_tmp      <= control_acc_aux;
      examples_completed   <= repeated;
      result_accumulate_tmp<= counter_result_address;
      less_distance_tmp    <= counter_less_distance_address;
      end_knn              <= end_accumulate_loop;
   end process;
end arq;