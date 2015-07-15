public class Main
{
	public static boolean branch;
	public static boolean word;
	public static void FindFunc(int x, boolean Itype)
	{
		switch (x)
		{
		case 32:
			if (Itype == false)
			{
				System.out.print(" add  ");
			}
			else
			{
				System.out.print(" lb  ");
			}
			break;
		case 34:
			System.out.print(" sub  ");
			break;
		case 36:
			System.out.print(" lb  ");
			break;
		case 37:
			System.out.print(" or   ");
			break;
		case 35:
			System.out.print(" lw  ");
			branch = false;
			word = true;
			break;
		case 43:
			System.out.print(" sw  ");
			branch = false;
			word = true;
			break;
		case 40:
			System.out.print(" sb  ");
			branch = false;
			word = true;
			break;
		case 4:
			System.out.print(" beq  ");
			branch = true;
			word = false;
			break;
		case 5:
			System.out.print(" bne  ");
			branch = true;
			word = false;
			break;
		default:
			System.out.print("  ? ");
			System.out.print(x);
		}


	}

	public static short[] main_memory = new short[2048];
	private static short InMM_x = 0;
	private static short InMM_y = 0;
	public static void InitMM()
	{
		//Initialize 
		for (int InitMM_x = 0;InitMM_x < 256;InitMM_x++)
		{
			main_memory[InitMM_x] = (short) InitMM_x;
		}
		for (InMM_x = 0; InMM_x < 256;InMM_x++)
		{ // initialize
			main_memory[InMM_x] = InMM_x;
		}
		for (short InitMM_y = 0 ; InMM_x < 512;InMM_x++)
		{
			main_memory[InMM_x] = InitMM_y;
			InitMM_y++;
		}
		for (InMM_y = 0; InMM_x < 768;InMM_x++)
		{
			main_memory[InMM_x] = InMM_y;
			InMM_y++;
		}
		for (InMM_y = 0; InMM_x < 1024;InMM_x++)
		{
			main_memory[InMM_x] = InMM_y;
			InMM_y++;
		}
		for (InMM_y = 0 ; InMM_x < 1280;InMM_x++)
		{
			main_memory[InMM_x] = InMM_y;
			InMM_y++;
		}
		for (InMM_y = 0; InMM_x < 1536;InMM_x++)
		{
			main_memory[InMM_x] = InMM_y;
			InMM_y++;
		}
		for (InMM_y = 0; InMM_x < 1792;InMM_x++)
		{
			main_memory[InMM_x] = InMM_y;
			InMM_y++;
		}
		for (InMM_y = 0 ; InMM_x < 2048;InMM_x++)
		{
			main_memory[InMM_x] = InMM_y;
			InMM_y++;
		}

	}

	public static int[] Registers = new int[32];

	public static void InitReg()
	{
		Registers[0] = 0;

		for (int x = 1; x < 32;x++)
		{
			Registers[x] = 256 + x;
		}
	}

	public static int opcode;
	public static int src1reg;
	public static int src2reg;
	public static int destreg;
	public static int funct;
	public static int PC;
	public static short offset;
	public static short branchOffset;

	public static int[] InstructionCache = {0xa1020000, 0x810AFFFC, 0x00831820, 0x01263820, 0x01224820, 0x81180000, 0x81510010, 0x00624022, 0x00000000, 0x00000000, 0x00000000, 0x00000000};
	public static IF_ID_Write IF_ID_W = new IF_ID_Write(); 
	public static IF_ID_Read IF_ID_R = new IF_ID_Read(); 
	public static ID_EX_Write ID_EX_W = new ID_EX_Write();
	public static ID_EX_Read ID_EX_R = new ID_EX_Read(); 
	public static EX_MEM_Read EX_MEM_R = new EX_MEM_Read(); 
	public static EX_MEM_Write EX_MEM_W = new EX_MEM_Write(); 
	public static MEM_WB_Write MEM_WB_W = new MEM_WB_Write(); 
	public static MEM_WB_Read MEM_WB_R = new MEM_WB_Read();

	public static void IF_stage(int index)
	{
		IF_ID_W.Inst = InstructionCache[index];
	}

	public static void ID_stage(int index)
	{
		src1reg = (IF_ID_R.Inst & 0x03E00000) >> 21;
		src2reg = (IF_ID_R.Inst & 0x001F0000) >> 16;
		opcode = (IF_ID_R.Inst & 0xFC000000) >> 26;
		destreg = (IF_ID_R.Inst & 0xF800) >> 11;
		funct = (IF_ID_R.Inst & 0x3F);
		offset = (short) (IF_ID_R.Inst & 0xFFFF);

		ID_EX_W.ReadReg1Value = src1reg;
		ID_EX_W.ReadReg2Value = src2reg;

		ID_EX_W.SEOffset = offset;

		branchOffset = (short) ((offset - 4) * 2); //subtract 4  shift left 2
		if (opcode == 0)
		{
			if (funct == 34 || funct == 32) //if R-format 
			{
				ID_EX_W.WriteRegNum = destreg;

				if (funct == 34)
				{
					ID_EX_W.func = (byte)'s';
				}

				if (funct == 32)
				{
					ID_EX_W.func = (byte)'a';
				}

				ID_EX_W.RegDst = (byte)'1';
				ID_EX_W.ALUSrc = (byte)'0';
				ID_EX_W.ALUOp1 = (byte)'1';
				ID_EX_W.ALUOp2 = (byte)'0';
				ID_EX_W.MemRead = (byte)'0';
				ID_EX_W.MemWrite = (byte)'0';
				ID_EX_W.Branch = (byte)'0';
				ID_EX_W.MemToReg = (byte)'0';
				ID_EX_W.RegWrite = (byte)'1';
			}

		}

		if (opcode == 35 || opcode == 32) //if lb
		{
			ID_EX_W.WriteRegNum = src2reg;
			ID_EX_W.RegDst = (byte)'0';
			ID_EX_W.ALUSrc = (byte)'1';
			ID_EX_W.ALUOp1 = (byte)'0';
			ID_EX_W.ALUOp2 = (byte)'0';
			ID_EX_W.MemRead = (byte)'1';
			ID_EX_W.MemWrite = (byte)'0';
			ID_EX_W.Branch = (byte)'0';
			ID_EX_W.MemToReg = (byte)'1';
			ID_EX_W.RegWrite = (byte)'1';

		}

		if (opcode == 43 || opcode == 40) //if sb 
		{
			ID_EX_W.WriteRegNum = src2reg;
			ID_EX_W.RegDst = (byte)'x';
			ID_EX_W.ALUSrc = (byte)'1';
			ID_EX_W.ALUOp1 = (byte)'0';
			ID_EX_W.ALUOp2 = (byte)'0';
			ID_EX_W.MemRead = (byte)'0';
			ID_EX_W.MemWrite = (byte)'1';
			ID_EX_W.Branch = (byte)'0';
			ID_EX_W.MemToReg = (byte)'x';
			ID_EX_W.RegWrite = (byte)'0';

		}
		if (src1reg == 0 && src2reg == 0 )
		{
			ID_EX_W.WriteRegNum = 0;
			ID_EX_W.RegDst = (byte)'x';
			ID_EX_W.ALUSrc = (byte)'x';
			ID_EX_W.ALUOp1 = (byte)'x';
			ID_EX_W.ALUOp2 = (byte)'x';
			ID_EX_W.MemRead = (byte)'x';
			ID_EX_W.MemWrite = (byte)'x';
			ID_EX_W.Branch = (byte)'x';
			ID_EX_W.MemToReg = (byte)'x';
			ID_EX_W.RegWrite = (byte)'x';
		}
		
	}

	public static void EX_stage()
	{

		EX_MEM_W.WriteRegNum = ID_EX_R.WriteRegNum;
		EX_MEM_W.SWValue = Registers[ID_EX_R.ReadReg2Value];

		if (ID_EX_W.func == 'a')
		{
			EX_MEM_W.ALUResult = Registers[ID_EX_R.ReadReg1Value] + Registers[ID_EX_R.ReadReg2Value];
		}
		if (ID_EX_W.func == 's')
		{

			EX_MEM_W.ALUResult = Registers[ID_EX_R.ReadReg1Value] - Registers[ID_EX_R.ReadReg2Value];
		}

		if (ID_EX_W.func == 'a' || ID_EX_W.func == 's')
		{
			EX_MEM_W.MemRead = ID_EX_R.MemRead;
			EX_MEM_W.MemWrite = ID_EX_R.MemWrite;
			EX_MEM_W.Branch = ID_EX_R.Branch;
			EX_MEM_W.MemToReg = ID_EX_R.MemToReg;
			EX_MEM_W.RegWrite = ID_EX_R.RegWrite;
			EX_MEM_W.WriteRegNum = ID_EX_R.WriteRegNum;
		}

		if (ID_EX_R.MemRead == '1')
		{
			EX_MEM_W.ALUResult = Registers[ID_EX_R.SEOffset + ID_EX_R.ReadReg1Value];

		}
		if (ID_EX_R.MemRead == '0' && ID_EX_R.MemWrite == '1')
		{
			EX_MEM_W.ALUResult = ID_EX_R.ReadReg1Value + ID_EX_R.SEOffset;

		}
	}

	public static void MEM_stage()
	{
		MEM_WB_W.ALUResult = EX_MEM_R.ALUResult;
		MEM_WB_W.ALUOp1 = EX_MEM_R.ALUOp1;
		MEM_WB_W.ALUOp2 = EX_MEM_R.ALUOp2;
		MEM_WB_W.RegDst = EX_MEM_R.RegDst;
		MEM_WB_W.ALUSrc = EX_MEM_R.ALUSrc;
		MEM_WB_W.ALUOp1 = EX_MEM_R.ALUOp1;
		MEM_WB_W.ALUOp2 = EX_MEM_R.ALUOp2;
		MEM_WB_W.MemRead = EX_MEM_R.MemRead;
		MEM_WB_W.MemWrite = EX_MEM_R.MemWrite;
		MEM_WB_W.Branch = EX_MEM_R.Branch;
		MEM_WB_W.MemToReg = EX_MEM_R.MemToReg;
		MEM_WB_W.RegWrite = EX_MEM_R.RegWrite;
		MEM_WB_W.WriteRegNum = EX_MEM_R.WriteRegNum;

		if (MEM_WB_W.MemToReg == '1' && MEM_WB_W.RegWrite == '1')
		{
			System.out.print(main_memory[EX_MEM_R.ALUResult]);
			MEM_WB_W.LWDataValue = main_memory[EX_MEM_R.ALUResult] % 100;

			System.out.print(MEM_WB_W.LWDataValue);
			System.out.print("\n");
		}
		if (MEM_WB_W.MemWrite == '1' && MEM_WB_W.RegWrite == '0')
		{
			main_memory[EX_MEM_W.ALUResult] = (short) EX_MEM_W.SWValue;
		}
	}

	public static void WB_stage()
	{
		if (MEM_WB_R.MemToReg == '1' && MEM_WB_R.RegWrite == '1') 
		{
			Registers[MEM_WB_R.WriteRegNum] = main_memory[MEM_WB_W.ALUResult] % 100;
		}

		if (MEM_WB_R.MemToReg == '0' && MEM_WB_R.RegWrite == '1') 
		{
			Registers[MEM_WB_R.WriteRegNum] = MEM_WB_R.ALUResult;

		}  
		if (MEM_WB_R.RegWrite == '0' && MEM_WB_R.MemToReg == 'x') 
		{
			main_memory[MEM_WB_W.ALUResult] = (short) (EX_MEM_R.SWValue % 100);
		}
	}

	public static void Printer(int index)
	{
		System.out.print("Kareem Zaiter Pipeline Project CS472");
		System.out.print("\n");
		System.out.print("\n Clock Cycle ");
		System.out.print(index + 1);
		System.out.print("\n");

		System.out.print(" IF/ID_Write (written to by the IF stage) ");
		System.out.print("\n");
		System.out.print("\n");
		System.out.print(" Instruction ");
		System.out.printf("%x",IF_ID_W.Inst);
		System.out.print("\n");
		System.out.print("\n");

		System.out.print(" IF/ID_Read(read to by the ID stage) ");
		System.out.print("\n");
		System.out.print("\n");
		System.out.print(" Instruction ");
		System.out.printf("%x",IF_ID_R.Inst);
		System.out.print("\n");
		System.out.print("\n");

		System.out.print("\n ID/EX_Write (written to by the ID stage) ");
		System.out.print("\n");
		System.out.print(" Control ");
		System.out.print(" RegDest = ");
		System.out.print(ID_EX_W.RegDst);
		System.out.print(" ALUsrc = ");
		System.out.print(ID_EX_W.ALUSrc);
		System.out.print(" ALUOP = ");
		System.out.print(ID_EX_W.ALUOp1);
		System.out.print(ID_EX_W.ALUOp2);
		System.out.print(" MEMRead = ");
		System.out.print(ID_EX_W.MemRead);
		System.out.print(" MEMWrite = ");
		System.out.print(ID_EX_W.MemWrite);
		System.out.print(" Branch = ");
		System.out.print(ID_EX_W.Branch);
		System.out.print(" MEMtoReg = ");
		System.out.print(ID_EX_W.MemToReg);
		System.out.print(" RegWrite = ");
		System.out.print(ID_EX_W.RegWrite);
		System.out.print("\n");
		System.out.print(" ReadReg1Value = ");
		System.out.printf("%d", Registers[ID_EX_W.ReadReg1Value]);
		System.out.print(" ReadReg2Value = ");
		System.out.printf("%d", Registers[ID_EX_W.ReadReg2Value]);
		System.out.print("\n");
		System.out.print(" WriteRegNum = ");
		System.out.print(ID_EX_W.WriteRegNum);
		System.out.print(" Offset = ");
		System.out.print(ID_EX_W.SEOffset);

		System.out.print("\n");
		System.out.print("\n");
		System.out.print("\n ID/EX_Read (read  by the EX stage) ");
		System.out.print("\n");

		System.out.print(" Control ");
		System.out.print(" RegDest = ");
		System.out.print(ID_EX_R.RegDst);
		System.out.print(" ALUsrc = ");
		System.out.print(ID_EX_R.ALUSrc);
		System.out.print(" ALUOP = ");
		System.out.print(ID_EX_R.ALUOp1);
		System.out.print(ID_EX_R.ALUOp2);
		System.out.print(" MEMRead = ");
		System.out.print(ID_EX_R.MemRead);
		System.out.print(" MEMWrite = ");
		System.out.print(ID_EX_R.MemWrite);
		System.out.print(" Branch = ");
		System.out.print(ID_EX_R.Branch);
		System.out.print(" MEMtoReg = ");
		System.out.print(ID_EX_R.MemToReg);
		System.out.print(" RegWrite = ");
		System.out.print(ID_EX_R.RegWrite);
		System.out.print("\n");
		System.out.print(" ReadReg1Value = ");
		System.out.printf("%d", Registers[ID_EX_R.ReadReg1Value]);
		System.out.print(" ReadReg2Value = ");
		System.out.printf("%d", Registers[ID_EX_R.ReadReg2Value]);
		System.out.print("\n");
		System.out.print(" WriteRegNum = ");
		System.out.print(ID_EX_R.WriteRegNum);
		System.out.print(" Offset = ");
		System.out.print(ID_EX_R.SEOffset);

		System.out.print("\n");
		System.out.print("\n");
		System.out.print("\n EX/MEM_Write (written to by the EX stage) ");
		System.out.print("\n");

		System.out.print(" Control ");
		System.out.print(" MEMRead = ");
		System.out.print(EX_MEM_W.MemRead);
		System.out.print(" MEMWrite = ");
		System.out.print(EX_MEM_W.MemWrite);
		System.out.print(" Branch = ");
		System.out.print(EX_MEM_W.Branch);
		System.out.print(" MEMtoReg = ");
		System.out.print(EX_MEM_W.MemToReg);
		System.out.print(" RegWrite = ");
		System.out.print(EX_MEM_W.RegWrite);
		if (EX_MEM_W.MemWrite == '1' && EX_MEM_W.MemToReg == 'x')
		{
			System.out.print("\n SB");
			System.out.print("\n");
		}
		if (EX_MEM_W.MemWrite == '0' && EX_MEM_W.MemRead == '1')
		{
			System.out.print("\n LB");
			System.out.print("\n");
		}

		System.out.print("\n");
		System.out.print(" ALUResult = ");
		System.out.print(EX_MEM_W.ALUResult);
		System.out.print("\n");
		System.out.print(" SWValue = ");
		System.out.print(EX_MEM_W.SWValue);
		System.out.print("\n");
		System.out.print(" WriteRegNum = ");
		System.out.print(EX_MEM_W.WriteRegNum);

		System.out.print("\n");
		System.out.print("\n");
		System.out.print("\n EX/MEM_Read (read by the MEM stage) ");
		System.out.print("\n");

		System.out.print(" Control ");
		System.out.print(" MEMRead = ");
		System.out.print(EX_MEM_R.MemRead);
		System.out.print(" MEMWrite = ");
		System.out.print(EX_MEM_R.MemWrite);
		System.out.print(" Branch = ");
		System.out.print(EX_MEM_R.Branch);
		System.out.print(" MEMtoReg = ");
		System.out.print(EX_MEM_R.MemToReg);
		System.out.print(" RegWrite = ");
		System.out.print(EX_MEM_R.RegWrite);

		if (EX_MEM_R.MemWrite == '1' && EX_MEM_R.MemToReg == 'x')
		{
			System.out.print("\n SB");
			System.out.print("\n");
		}
		if (EX_MEM_R.MemWrite == '0' && EX_MEM_R.MemRead == '1')
		{
			System.out.print("\n LB");
			System.out.print("\n");
		}

		System.out.print("\n");
		System.out.print(" ALUResult = ");
		System.out.print(EX_MEM_R.ALUResult);
		System.out.print("\n");
		System.out.print(" SWValue = ");
		System.out.print(EX_MEM_R.SWValue);
		System.out.print("\n");
		System.out.print(" WriteRegNum = ");
		System.out.print(EX_MEM_R.WriteRegNum);

		System.out.print("\n");
		System.out.print("\n");
		System.out.print("\n MEM/WB_Write (written to by the MEM stage) ");
		System.out.print("\n");

		if (MEM_WB_W.MemWrite == '1' && MEM_WB_W.MemToReg == 'x')
		{
			System.out.print("\n SB");
			System.out.print("\n");
		}
		if (MEM_WB_W.MemWrite == '0' && MEM_WB_W.MemRead == '1')
		{
			System.out.print("\n LB");
			System.out.print("\n");
		}
		System.out.print(" Control ");
		System.out.print(" MEMtoReg = ");
		System.out.print(MEM_WB_W.MemToReg);
		System.out.print(" RegWrite = ");
		System.out.print(MEM_WB_W.RegWrite);
		System.out.print("\n");
		System.out.print(" LWValue   = ");
		System.out.print(MEM_WB_W.LWDataValue);
		System.out.print("\n");
		System.out.print(" ALUResult = ");
		System.out.print(MEM_WB_W.ALUResult);
		System.out.print("\n");
		System.out.print(" WriteRegNum = ");
		System.out.print(MEM_WB_W.WriteRegNum);
		System.out.print("\n");


		System.out.print("\n");
		System.out.print("\n");
		System.out.print("\n MEM/WB_Read (read  by the WB stage) ");
		System.out.print("\n");

		if (MEM_WB_R.MemWrite == '1' && MEM_WB_R.MemToReg == 'x')
		{
			System.out.print("\n SB");
			System.out.print("\n");
		}
		if (MEM_WB_R.MemWrite == '0' && MEM_WB_R.MemRead == '1')
		{
			System.out.print("\n LB");
			System.out.print("\n");
		}

		System.out.print(" Control ");
		System.out.print(" MEMtoReg = ");
		System.out.print(MEM_WB_R.MemToReg);
		System.out.print(" RegWrite = ");
		System.out.print(MEM_WB_R.RegWrite);
		System.out.print("\n");
		System.out.print(" LWValue   = ");
		System.out.print(MEM_WB_R.LWDataValue);
		System.out.print("\n");
		System.out.print(" ALUResult = ");
		System.out.print(MEM_WB_R.ALUResult);
		System.out.print("\n");
		System.out.print(" WriteRegNum = ");
		System.out.print(MEM_WB_R.WriteRegNum);
		System.out.print("\n");
		System.out.print("\n");
		System.out.print("Register Values:");
		System.out.print("\n");


		for (int x = 0;x < 32;x++)
		{
			System.out.print("Registers: ");
			System.out.print(Registers[x]);
			System.out.print("\n");
		}

	}

	public static void Copy_Write_to_Read(int index)
	{
		IF_ID_R.Inst = IF_ID_W.Inst; //Copy Write To Read

		ID_EX_R.ALUOp1 = ID_EX_W.ALUOp1;
		ID_EX_R.ALUOp2 = ID_EX_W.ALUOp2;
		ID_EX_R.RegDst = ID_EX_W.RegDst;
		ID_EX_R.ALUSrc = ID_EX_W.ALUSrc;
		ID_EX_R.ALUOp1 = ID_EX_W.ALUOp1;
		ID_EX_R.ALUOp2 = ID_EX_W.ALUOp2;
		ID_EX_R.MemRead = ID_EX_W.MemRead;
		ID_EX_R.MemWrite = ID_EX_W.MemWrite;
		ID_EX_R.Branch = ID_EX_W.Branch;
		ID_EX_R.MemToReg = ID_EX_W.MemToReg;
		ID_EX_R.RegWrite = ID_EX_W.RegWrite;
		ID_EX_R.ReadReg1Value = ID_EX_W.ReadReg1Value;
		ID_EX_R.ReadReg2Value = ID_EX_W.ReadReg2Value;
		ID_EX_R.SEOffset = ID_EX_W.SEOffset;
		ID_EX_R.Function = ID_EX_W.Function;
		ID_EX_R.Rtype = ID_EX_W.Rtype;
		ID_EX_R.WriteRegNum = ID_EX_W.WriteRegNum;

		EX_MEM_R.ALUResult = EX_MEM_W.ALUResult;
		EX_MEM_R.ALUOp1 = EX_MEM_W.ALUOp1;
		EX_MEM_R.ALUOp2 = EX_MEM_W.ALUOp2;
		EX_MEM_R.RegDst = EX_MEM_W.RegDst;
		EX_MEM_R.ALUSrc = EX_MEM_W.ALUSrc;
		EX_MEM_R.ALUOp1 = EX_MEM_W.ALUOp1;
		EX_MEM_R.ALUOp2 = EX_MEM_W.ALUOp2;
		EX_MEM_R.MemRead = EX_MEM_W.MemRead;
		EX_MEM_R.MemWrite = EX_MEM_W.MemWrite;
		EX_MEM_R.Branch = EX_MEM_W.Branch;
		EX_MEM_R.MemToReg = EX_MEM_W.MemToReg;
		EX_MEM_R.RegWrite = EX_MEM_W.RegWrite;
		EX_MEM_R.WriteRegNum = EX_MEM_W.WriteRegNum;
		EX_MEM_R.SWValue = EX_MEM_W.SWValue;

		MEM_WB_R.ALUResult = MEM_WB_W.ALUResult;
		MEM_WB_R.ALUOp1 = MEM_WB_W.ALUOp1;
		MEM_WB_R.ALUOp2 = MEM_WB_W.ALUOp2;
		MEM_WB_R.RegDst = MEM_WB_W.RegDst;
		MEM_WB_R.ALUSrc = MEM_WB_W.ALUSrc;
		MEM_WB_R.ALUOp1 = MEM_WB_W.ALUOp1;
		MEM_WB_R.ALUOp2 = MEM_WB_W.ALUOp2;
		MEM_WB_R.MemRead = MEM_WB_W.MemRead;
		MEM_WB_R.MemWrite = MEM_WB_W.MemWrite;
		MEM_WB_R.Branch = MEM_WB_W.Branch;
		MEM_WB_R.MemToReg = MEM_WB_W.MemToReg;
		MEM_WB_R.RegWrite = MEM_WB_W.RegWrite;
		MEM_WB_R.LWDataValue = MEM_WB_W.LWDataValue;
		MEM_WB_R.WriteRegNum = MEM_WB_W.WriteRegNum;
	}

	public static void main(String[] args)
	{
		Main.InitMM(); //inMM
		Main.InitReg(); //inReg

		ID_EX_W.RegDst = 0;
		ID_EX_W.ALUSrc = 0;
		ID_EX_W.ALUOp1 = 0;
		ID_EX_W.ALUOp2 = 0;
		ID_EX_W.MemRead = 0;
		ID_EX_W.MemWrite = 0;
		ID_EX_W.Branch = 0;
		ID_EX_W.MemToReg = 0;
		ID_EX_W.RegWrite = 0;



		ID_EX_R.RegDst = 0;
		ID_EX_R.ALUSrc = 0;
		ID_EX_R.ALUOp1 = 0;
		ID_EX_R.ALUOp2 = 0;
		ID_EX_R.MemRead = 0;
		ID_EX_R.MemWrite = 0;
		ID_EX_R.Branch = 0;
		ID_EX_R.MemToReg = 0;
		ID_EX_R.RegWrite = 0;



		EX_MEM_W.RegDst = 0;
		EX_MEM_W.ALUSrc = 0;
		EX_MEM_W.ALUOp1 = 0;
		EX_MEM_W.ALUOp2 = 0;
		EX_MEM_W.MemRead = 0;
		EX_MEM_W.MemWrite = 0;
		EX_MEM_W.Branch = 0;
		EX_MEM_W.MemToReg = 0;
		EX_MEM_W.RegWrite = 0;


		EX_MEM_R.RegDst = 0;
		EX_MEM_R.ALUSrc = 0;
		EX_MEM_R.ALUOp1 = 0;
		EX_MEM_R.ALUOp2 = 0;
		EX_MEM_R.MemRead = 0;
		EX_MEM_R.MemWrite = 0;
		EX_MEM_R.Branch = 0;
		EX_MEM_R.MemToReg = 0;
		EX_MEM_R.RegWrite = 0;


		MEM_WB_W.RegDst = 0;
		MEM_WB_W.ALUSrc = 0;
		MEM_WB_W.ALUOp1 = 0;
		MEM_WB_W.ALUOp2 = 0;
		MEM_WB_W.MemRead = 0;
		MEM_WB_W.MemWrite = 0;
		MEM_WB_W.Branch = 0;
		MEM_WB_W.MemToReg = 0;
		MEM_WB_W.RegWrite = 0;


		MEM_WB_R.RegDst = 0;
		MEM_WB_R.ALUSrc = 0;
		MEM_WB_R.ALUOp1 = 0;
		MEM_WB_R.ALUOp2 = 0;
		MEM_WB_R.MemRead = 0;
		MEM_WB_R.MemWrite = 0;
		MEM_WB_R.Branch = 0;
		MEM_WB_R.MemToReg = 0;
		MEM_WB_R.RegWrite = 0;

		for (int index = 0;index <=11;index++)
		{
			Main.IF_stage(index);

			Main.ID_stage(index);

			Main.EX_stage();

			Main.MEM_stage();

			Main.WB_stage();

			Main.Printer(index);

			Main.Copy_Write_to_Read(index);

		}

	}
}
