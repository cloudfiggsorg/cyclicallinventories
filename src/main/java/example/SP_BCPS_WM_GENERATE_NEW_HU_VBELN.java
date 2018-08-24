package example;

public class SP_BCPS_WM_GENERATE_NEW_HU_VBELN {

//	CREATE PROCEDURE [dbo].[sp_bcps_wm_generate_new_hu_vbeln]
//			@AUFNR nvarchar(20), @ERLKZ nvarchar(50), @VHILM nvarchar(35), @MATNR nvarchar(35), @MEINS nvarchar(18), @VEMNG nvarchar(10), @PACKNR nvarchar(35), @WERKS nvarchar(35), @CHARG nvarchar(25), @CHARG2 nvarchar(25), @VEMNG2 nvarchar(10), @MEINS2 nvarchar(18), @USERID nvarchar(18), @RETURN int OUTPUT
//			WITH EXEC AS CALLER
//			AS
//			DECLARE @VALIDATE INT
//			  SET @VALIDATE = (SELECT COUNT(VBELN) FROM LIKP WHERE VBELN = @AUFNR)
//			  IF(@VALIDATE > 0)
//			  BEGIN
//			    DECLARE @VENUM NVARCHAR(10)
//			  	DECLARE @NHUV NVARCHAR(20)
//			  	DECLARE @NHUB BIGINT
//			  	DECLARE @MINUSHU BIGINT
//			  	DECLARE @MINUSVEN BIGINT
//			  	DECLARE @LGORT NVARCHAR(25)
//			  	DECLARE @LGNUM NVARCHAR(25)
//			  	SET @MINUSHU = 5000000000000000000
//			  	SET @MINUSVEN = 5000000000
//			  	SET @NHUB = (SELECT MAX(HU) FROM TB_BCPS_NEW_HU WITH(NOLOCK))
//			  	SET @NHUB =  @NHUB + 1
//			  	SET @NHUV = '0' + CONVERT(NVARCHAR(19), @NHUB)
//			  	SET @VENUM = CONVERT(NVARCHAR(10), ( @MINUSVEN + ( @NHUB - @MINUSHU ) ) )
//			  	IF(@WERKS = 'PC01')
//			  	BEGIN
//			  		SET @LGORT = (SELECT DISTINCT TOP 1 ALMACEN FROM centrosAlmacenesPermitidos WITH(NOLOCK) WHERE centro = @WERKS)
//			  	END
//			  	ELSE
//			  	BEGIN
//			  		SET @LGORT = (SELECT DISTINCT(ALMACEN) FROM centrosAlmacenesPermitidos WITH(NOLOCK) WHERE centro = @WERKS)
//			  	END
//			  	SET @LGNUM = (SELECT DISTINCT(noAlmacen) FROM centrosAlmacenesPermitidos WITH(NOLOCK) WHERE centro = @WERKS AND almacen = @LGORT)
//			  	IF( (SELECT COUNT(*) FROM TB_BCPS_NEW_HU WITH(NOLOCK) WHERE HU = @NHUB) > 0 )
//			    BEGIN
//			      EXEC SP_BCPS_WM_GENERATE_NEW_HU_VBELN @AUFNR, @ERLKZ, @VHILM, @MATNR, @MEINS, @VEMNG, @PACKNR, @WERKS, @CHARG, @CHARG2, @VEMNG2, @MEINS2, @RETURN = @RETURN OUT
//			    END
//			    ELSE
//			    BEGIN
//			      BEGIN TRANSACTION    
//			      	BEGIN TRY
//			        INSERT INTO TB_BCPS_NEW_HU(HU,VENUM,AUFNR) VALUES(@NHUB, @VENUM, @AUFNR)
//			      	IF(@@ROWCOUNT = 1)
//			      	BEGIN
//			      		INSERT INTO VEKP(VENUM,EXIDV,VHILM,MEINS,WERKS,VPOBJKEY,LGNUM,PACKVORSCHR,ERLKZ) VALUES(@VENUM, @NHUV, @VHILM, @MEINS2, @WERKS, @AUFNR,@LGNUM, @PACKNR, @ERLKZ)
//			      		IF(@@ROWCOUNT = 1)
//			      		BEGIN
//			      			INSERT INTO VEPO(VENUM,VEPOS,VELIN,POSNR,VEMNG,VEMEH,MATNR,CHARG,WERKS,LGORT,VEANZ,VBELN ) VALUES(@VENUM,'000010','1','000010',@VEMNG2, @MEINS2,@MATNR,@CHARG,@WERKS,@LGORT, @CHARG2,@AUFNR)
//			      			IF(@@ROWCOUNT = 1)
//			      			BEGIN
//			              
//			              INSERT INTO zContingencia(IDPROC,FECHA,HORA,CENTRO,HU, ENTREGA ,CANTIDAD,USUARIO,TARIMA,UNIDADMEDIDA,LOTE1,LOTE2,NORMAEMBALAJE ) values (21, (convert(date,getdate())), (convert(time, getdate())),@WERKS,@NHUV,@AUFNR,@VEMNG,@USERID,@VHILM,@MEINS,@CHARG,@CHARG2,@PACKNR) 
//			              IF(@@ROWCOUNT = 1)
//			      			  BEGIN
//			              	SET @RETURN = 1
//			      				  COMMIT
//			              END
//			              ELSE
//			      			  BEGIN
//			      				  SET @RETURN = 4
//			      				  ROLLBACK
//			      			  END
//			      			END
//			      			ELSE
//			      			BEGIN
//			      				SET @RETURN = 4
//			      				ROLLBACK
//			      			END
//			      		END
//			      		ELSE
//			      		BEGIN
//			      			SET @RETURN = 3
//			      			ROLLBACK
//			      		END
//			        END
//			      	ELSE
//			      	BEGIN
//			      		SET @RETURN = 2		
//			      		ROLLBACK
//			      	END
//			      END TRY
//			      BEGIN CATCH
//			        ROLLBACK
//			              EXEC SP_BCPS_WM_GENERATE_NEW_HU_VBELN @AUFNR, @ERLKZ, @VHILM, @MATNR, @MEINS, @VEMNG, @PACKNR, @WERKS, @CHARG, @CHARG2, @VEMNG2, @MEINS2, @RETURN = @RETURN OUT
//			      END CATCH
//			    END
//			  END
//			  ELSE
//			  BEGIN
//			    SET @RETURN = 10
//			  END
//			GO
}
