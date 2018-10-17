package com.animania.client.render.pigs;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.animania.client.models.ModelPiglet;
import com.animania.client.render.layer.LayerBlinking;
import com.animania.client.render.pigs.layers.LayerMudPigletOldSpot;
import com.animania.common.entities.pigs.EntityAnimaniaPig;
import com.animania.common.entities.pigs.EntityPigletOldSpot;
import com.animania.common.handler.BlockHandler;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPigletOldSpot<T extends EntityPigletOldSpot> extends RenderLiving<T>
{
	public static final Factory FACTORY = new Factory();

	private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("animania:textures/entity/pigs/piglet_old_spot.png");
	private static final ResourceLocation PIG_TEXTURES_BLINK = new ResourceLocation("animania:textures/entity/pigs/piglet_blink.png");

	public RenderPigletOldSpot(RenderManager rm)
	{
		super(rm, new ModelPiglet(), 0.3F);
		this.addLayer(new LayerMudPigletOldSpot(this));
		this.addLayer(new LayerBlinking(this, PIG_TEXTURES_BLINK, 0x514B4B));
	}

	protected void preRenderScale(T entity, float f)
	{
		float age = entity.getEntityAge();
		GL11.glScalef(1.1F + age, 1.1F + age, 1.1F + age);

		boolean isSleeping = false;
		EntityAnimaniaPig entityChk = (EntityAnimaniaPig) entity;

		if (entityChk.getSleeping())
		{
			isSleeping = true;
		}

		if (isSleeping)
		{
			this.shadowSize = 0;
			float sleepTimer = entityChk.getSleepTimer();
			if (entityChk.getRNG().nextInt(2) < 1 && sleepTimer > -0.55F)
			{
				sleepTimer = sleepTimer - 0.01F;
			}
			entity.setSleepTimer(sleepTimer);

			GlStateManager.translate(0.0F, entity.height - 0.70F + age * .1F, 0.0F);
			GlStateManager.rotate(86.0F, 0.0F, 0.0F, 1.0F);
		}
		else
		{
			entityChk.setSleeping(false);
			entityChk.setSleepTimer(0F);

			double x = entity.posX;
			double y = entity.posY;
			double z = entity.posZ;

			BlockPos pos = new BlockPos(x, y, z);
			Random rand = new Random();

			Block blockchk = entity.world.getBlockState(pos).getBlock();
			Block blockchk2 = entity.world.getBlockState(pos).getBlock();
			boolean mudBlock = false;
			if (blockchk == BlockHandler.blockMud || blockchk.getUnlocalizedName().contains("tile.mud") || blockchk2.getUnlocalizedName().contains("tile.mud"))
			{
				mudBlock = true;
			}

			if (mudBlock && !entity.getMuddy())
			{
				GlStateManager.translate(0.0F, entity.height - 0.8F + age * .1F, 0.0F);
				GlStateManager.rotate(86.0F, 0.0F, 0.0F, 1.0F);
				entity.setMuddy(true);
				entity.setMudTimer(1.0F);
				entity.setSplashTimer(1.0F);
			}
			else if (entity.isWet() && entity.getMuddy() && !mudBlock)
			{
				entity.setMuddy(false);
				entity.setMudTimer(0.0F);
				entity.setSplashTimer(0.0F);
			}
			else if (mudBlock)
			{
				Float splashTimer = entity.getSplashTimer();
				if (!entity.hasPath())
				{
					GlStateManager.translate(0.0F, entity.height - 0.8F + age * .1F, 0.0F);
					GlStateManager.rotate(86.0F, 0.0F, 0.0F, 1.0F);
				}
				splashTimer = splashTimer - 0.045F;
				entity.setSplashTimer(splashTimer);
				if (splashTimer <= 0.0F)
				{
					entity.setMuddy(true);
					entity.setMudTimer(1.0F);
				}
			}
			else if (entity.getMudTimer() > 0)
			{
				entity.setMuddy(false);
				float mudTimer = entity.getMudTimer();
				if (rand.nextInt(3) < 1)
				{
					mudTimer = mudTimer - 0.0025F;
					entity.setMudTimer(mudTimer);
				}
			}
		}

	}

	@Override
	protected void preRenderCallback(T entityliving, float f)
	{
		this.preRenderScale(entityliving, f);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		return this.PIG_TEXTURES;
	}

	static class Factory<T extends EntityPigletOldSpot> implements IRenderFactory<T>
	{
		@Override
		public Render<? super T> createRenderFor(RenderManager manager)
		{
			return new RenderPigletOldSpot(manager);
		}
	}
}