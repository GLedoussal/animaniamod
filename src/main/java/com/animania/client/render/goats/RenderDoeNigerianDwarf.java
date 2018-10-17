package com.animania.client.render.goats;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.animania.client.models.goats.ModelDoeNigerianDwarf;
import com.animania.client.render.layer.LayerBlinking;
import com.animania.common.entities.goats.EntityAnimaniaGoat;
import com.animania.common.entities.goats.EntityDoeNigerianDwarf;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDoeNigerianDwarf<T extends EntityDoeNigerianDwarf> extends RenderLiving<T>
{
	public static final Factory FACTORY = new Factory();
	private static final ResourceLocation goatTextures = new ResourceLocation("animania:textures/entity/goats/doe_nigerian.png");
	private static final ResourceLocation goatTexturesBlink = new ResourceLocation("animania:textures/entity/goats/goats_blink.png");
	Random rand = new Random();

	public RenderDoeNigerianDwarf(RenderManager rm)
	{
		super(rm, new ModelDoeNigerianDwarf(), 0.3F);
		this.addLayer(new LayerBlinking(this, goatTexturesBlink, 0x404040));
	}

	protected ResourceLocation getGoatTextures(T par1Entity)
	{
		return RenderDoeNigerianDwarf.goatTextures;
	}

	protected ResourceLocation getGoatTexturesBlink(T par1Entity)
	{
		return RenderDoeNigerianDwarf.goatTexturesBlink;
	}

	protected void preRenderScale(EntityDoeNigerianDwarf entity, float f)
	{
		GL11.glScalef(0.42F, 0.42F, 0.42F);
		GL11.glTranslatef(0f, 0f, -0.5f);
		GL11.glTranslatef(0f, 0f, -0.5f);
		boolean isSleeping = false;
		EntityAnimaniaGoat entityGoat = (EntityAnimaniaGoat) entity;
		if (entityGoat.getSleeping())
		{
			isSleeping = true;
		}

		if (isSleeping)
		{
			this.shadowSize = 0;
			float sleepTimer = entityGoat.getSleepTimer();
			if (sleepTimer > -0.55F)
			{
				sleepTimer = sleepTimer - 0.01F;
			}
			entity.setSleepTimer(sleepTimer);

			GlStateManager.translate(-0.25F, entity.height - 1.45F - sleepTimer, -0.25F);
			GlStateManager.rotate(6.0F, 0.0F, 0.0F, 1.0F);
		}
		else
		{
			this.shadowSize = 0.3F;
			entityGoat.setSleeping(false);
			entityGoat.setSleepTimer(0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		return this.getGoatTextures(entity);
	}

	@Override
	protected void preRenderCallback(T entityliving, float f)
	{
		this.preRenderScale(entityliving, f);
	}

	static class Factory<T extends EntityDoeNigerianDwarf> implements IRenderFactory<T>
	{
		@Override
		public Render<? super T> createRenderFor(RenderManager manager)
		{
			return new RenderDoeNigerianDwarf(manager);
		}

	}
}
