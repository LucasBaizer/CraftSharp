package com.craftsharp.api.ui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Random;

import com.craftsharp.CraftSharp;
import com.craftsharp.ModPacket;
import com.craftsharp.api.ForgeRuntimeException;
import com.craftsharp.api.ParticleColor;
import com.craftsharp.api.ParticleFX;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class Machine extends net.minecraft.block.BlockContainer {
	private IIcon currentIcon;
	private IIcon onIcon;
	private IIcon offIcon;
	private IIcon sideIcon;
	private IIcon[] icons = new IIcon[6];
	private boolean specialIcons;
	private ParticleColor particleColor;

	private Object modInstance;
	private int guiID;
	public String background;
	private int windowWidth = -1;
	private int windowHeight = -1;

	/**
	 * @param modInstance
	 *            A singleton, uninstantiated instance of your main mod class.
	 *            It should be defined as:
	 *            <code>@Instance<br>public static MainClass modInstance;</code>
	 * @param guid
	 *            The ID of this Machine. This will be recognized by the
	 *            {@link UIHandler} to open the GUI on the client side. Must be
	 *            unique.
	 */
	public Machine(Material material, Object modInstance, int guid, String background) {
		super(material);
		setModInstance(modInstance);
		setGuiID(guid);
		this.background = background;

		if (UIHandler.uiMap.containsKey(guid)) {
			throw new ForgeRuntimeException(
					"GUI ID " + guid + " already taken by " + UIHandler.uiMap.get(guid).getClass().getName()
							+ ", cannot be assigned to " + this.getClass().getName());
		}

		UIHandler.uiMap.put(guid, this);
	}

	public void setParticleColor(ParticleColor color) {
		this.particleColor = color;
	}

	public ParticleColor getParticleColor() {
		return this.particleColor;
	}

	public void initializeSpecialIcons() {
		specialIcons = true;
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		if (specialIcons) {
			onIcon = reg.registerIcon(this.textureName + "_front_on");
			currentIcon = offIcon = reg.registerIcon(this.textureName + "_front_off");

			this.sideIcon = reg.registerIcon(this.textureName + "_side");
			for (int i = 0; i < 6; i++) {
				this.icons[i] = this.sideIcon;
			}
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (specialIcons) {
			if (meta == 0) {
				return side == 3 ? offIcon : sideIcon;
			} else {
				try {
					if ((meta & 8) == 8) {
						this.currentIcon = onIcon;
						meta &= 7;
					} else {
						this.currentIcon = offIcon;
					}
					return side == meta ? this.currentIcon : sideIcon;
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("Meta is out of range! Meta value: " + meta);
					return super.getIcon(side, meta);
				}
			}
		}
		return super.getIcon(side, meta);
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (particleColor != null && (world.getBlockMetadata(x, y, z) & 8) == 8) {
			double d0 = 0.0625D;
			for (int l = 0; l < 6; ++l) {
				double d1 = (double) ((float) x + random.nextFloat());
				double d2 = (double) ((float) y + random.nextFloat());
				double d3 = (double) ((float) z + random.nextFloat());
				if (l == 0 && !world.getBlock(x, y + 1, z).isOpaqueCube())
					d2 = (double) (y + 1) + d0;
				if (l == 1 && !world.getBlock(x, y - 1, z).isOpaqueCube())
					d2 = (double) (y + 0) - d0;
				if (l == 2 && !world.getBlock(x, y, z + 1).isOpaqueCube())
					d3 = (double) (z + 1) + d0;
				if (l == 3 && !world.getBlock(x, y, z - 1).isOpaqueCube())
					d3 = (double) (z + 0) - d0;
				if (l == 4 && !world.getBlock(x + 1, y, z).isOpaqueCube())
					d1 = (double) (x + 1) + d0;
				if (l == 5 && !world.getBlock(x - 1, y, z).isOpaqueCube())
					d1 = (double) (x + 0) - d0;
				if (d1 < (double) x || d1 > (double) (x + 1) || d2 < 0.0D || d2 > (double) (y + 1) || d3 < (double) z
						|| d3 > (double) (z + 1)) {
					ParticleFX var20 = new ParticleFX(world, d1, d2, d3, particleColor.getRed(),
							particleColor.getGreen(), particleColor.getBlue());
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(var20);
				}
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
		switch (MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
			case 0:
				world.setBlockMetadataWithNotify(x, y, z, 2, 2); // north facing
				break;
			case 1:
				world.setBlockMetadataWithNotify(x, y, z, 5, 2); // east facing
				break;
			case 2:
				world.setBlockMetadataWithNotify(x, y, z, 3, 2); // south facing
				break;
			case 3:
				world.setBlockMetadataWithNotify(x, y, z, 4, 2); // west facing
				break;
			default:
				break;
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if (!world.isRemote) {
			Block block = world.getBlock(x, y, z - 1);
			Block block1 = world.getBlock(x, y, z + 1);
			Block block2 = world.getBlock(x - 1, y, z);
			Block block3 = world.getBlock(x + 1, y, z);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j()) {
				b0 = 3; // south facing
			}
			if (block1.func_149730_j() && !block.func_149730_j()) {
				b0 = 2; // north facing
			}
			if (block2.func_149730_j() && !block3.func_149730_j()) {
				b0 = 5; // east facing
			}
			if (block3.func_149730_j() && !block2.func_149730_j()) {
				b0 = 4; // west facing
			}

			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int v) {
		try {
			MachineTileEntity entity = ((MachineTileEntity) getTileEntityClass().newInstance());
			entity.inventoryName = getUnlocalizedName();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		if (!player.isSneaking()) {
			try {
				CraftSharp.channel
						.sendToAll(ModPacket.createMachinePacket((MachineTileEntity) world.getTileEntity(x, y, z)));
			} catch (IOException e) {
				e.printStackTrace();
				FMLCommonHandler.instance().exitJava(1, true);
			}
			player.openGui(modInstance, guiID, world, x, y, z);
		}
		return !player.isSneaking();
	}

	private static final Random random = new Random();

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		currentIcon = offIcon;

		MachineTileEntity tile = (MachineTileEntity) world.getTileEntity(x, y, z);
		if (tile != null) {
			for (int i = 0; i < tile.getSizeInventory(); ++i) {
				ItemStack item = tile.getStackInSlot(i);

				if (item != null) {
					float f = random.nextFloat() * 0.6F + 0.1F;
					float f1 = random.nextFloat() * 0.6F + 0.1F;
					float f2 = random.nextFloat() * 0.6F + 0.1F;

					EntityItem entity = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1),
							(double) ((float) z + f2),
							new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

					if (item.hasTagCompound()) {
						entity.getEntityItem().setTagCompound(((NBTTagCompound) item.getTagCompound().copy()));
					}

					float f3 = 0.025F;
					entity.motionX = (double) ((float) random.nextGaussian() * f3);
					entity.motionY = (double) ((float) random.nextGaussian() * f3 + 0.1F);
					entity.motionZ = (double) ((float) random.nextGaussian() * f3);
					world.spawnEntityInWorld(entity);
				}
			}
			world.func_147453_f(x, y, z, block);
		}

		super.breakBlock(world, x, y, z, block, meta);
	}

	public Object getModInstance() {
		return modInstance;
	}

	public void setModInstance(Object modInstance) {
		this.modInstance = modInstance;
	}

	public int getGuiID() {
		return guiID;
	}

	public void setGuiID(int guiID) {
		this.guiID = guiID;
	}

	public abstract Class<? extends TileEntity> getTileEntityClass();

	public abstract void drawTiles(MachineContainer container);

	public void drawToUI(MachineUI ui, MachineTileEntity entity) {
		if (ui.getCurrentUIScreen() != null)
			ui.getCurrentUIScreen().render(entity);
	}

	public void setWindowDimensions(Dimension dim) {
		this.windowWidth = dim.width;
		this.windowHeight = dim.height;
	}

	public Dimension getWindowDimensions() {
		return new Dimension(windowWidth, windowHeight);
	}

	public boolean hasAnySpecificDimensions() {
		return windowWidth != -1 || windowHeight != -1;
	}

	private ResourceLocation defaultBackground;

	public ResourceLocation getDefaultBackground() {
		return defaultBackground == null
				? (defaultBackground = new ResourceLocation(CraftSharp.MODID, "textures/ui/" + background + ".png"))
				: defaultBackground;
	}
}
