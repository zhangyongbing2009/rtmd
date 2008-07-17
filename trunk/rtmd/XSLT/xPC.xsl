<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- $Id: xPC.xsl,v 1.0 2006/03/10 14:31:58 tmm3 Exp $ -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" />

  <xsl:template match="/">

	<xPC>
        <xsl:for-each select="//NeesDaqConfiguration/ChannelParameter">
          <xsl:if test="@InTest=1">
            <xPCReadBlock name="{@Name}" unit="{@Units}" Gain="{@Gain}" VoltageSlope="{@VoltageSlope}" VoltageOffset="{@VoltageOffset}" EUSlope="{@Sensitivity}" EUOffset="{@Offset}" isDAQ="true">
              
              <xsl:variable name="id" select="@Channel" />
              <xsl:variable name="location" select="//NEESSim/Scramnet/DaqBlock[@ChannelID=$id]/@Offset" />
  
              <xsl:attribute name="location">
                <xsl:choose>
                  <xsl:when test="$location &gt; 0">                    
                    <xsl:value-of select="$location" /> 
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="-1" />
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
            </xPCReadBlock>
          </xsl:if>
        </xsl:for-each>
      
      
        <xsl:for-each select="//NEESsim/Scramnet/CtrlBlock">
          <xsl:if test="@Stream='true'">
            <xPCReadBlock name="{@Description}" unit="{@Units}" Gain="{@Scale}" location="{@Offset}" isDAQ="false" />
          </xsl:if>
        </xsl:for-each>     
        
        <xsl:for-each select="//NEESsim/Scramnet/SimBlock">
          <xPCReadBlock name="Sim_{@Name}" unit="n_a" Gain="1" location="{@Offset}" isDAQ="false" />
        </xsl:for-each>           
           
		<xsl:for-each select="//NEESsim/Scramnet/CtrlBlock">
          <xsl:if test="(@Stream='true') and (@Offset &lt; 64)">
            <xPCWriteBlock name="{@Description}" unit="{@Units}" Gain="{@Scale}" location="{@Offset}" lowerlimit="{@LowerLimit}" upperlimit="{@UpperLimit}" isCTRL = "true" />
          </xsl:if>
        </xsl:for-each>           
        
        <xsl:for-each select="//NEESsim/Scramnet/SimBlock">        
          <xPCWriteBlock name="Sim_{@Name}_write" unit="n_a" Gain="1" location="{@Offset}" isCTRL = "false" />
        </xsl:for-each>                    
                
        <xsl:for-each select="//NEESsim/Scramnet/NodeBlock[not(@ConstraintID = preceding-sibling::NodeBlock/@ConstraintID)]">
        <xsl:sort select="@ConstraintID" />
		  <xsl:if test="number(@DXOffset) &gt; 999">
			<xPCWriteBlock name="Node_{@ID}_DX" unit="{@DUnits}" Gain="1" location="{@DXOffset}" isCTRL = "false" />
		  </xsl:if>
		  <xsl:if test="number(@DYOffset) &gt; 999">
			<xPCWriteBlock name="Node_{@ID}_DY" unit="{@DUnits}" Gain="1" location="{@DYOffset}" isCTRL = "false" />
		  </xsl:if>
		  <xsl:if test="number(@DZOffset) &gt; 999">
			<xPCWriteBlock name="Node_{@ID}_DZ" unit="{@DUnits}" Gain="1" location="{@DZOffset}" isCTRL = "false" />
		  </xsl:if>
		  <xsl:if test="number(@TXOffset) &gt; 999">
			<xPCWriteBlock name="Node_{@ID}_TX" unit="{@TUnits}" Gain="1" location="{@TXOffset}" isCTRL = "false" />
		  </xsl:if>
		  <xsl:if test="number(@TYOffset) &gt; 999">
			<xPCWriteBlock name="Node_{@ID}_TY" unit="{@TUnits}" Gain="1" location="{@TYOffset}" isCTRL = "false" />
		  </xsl:if>
		  <xsl:if test="number(@TZOffset) &gt; 999">
			<xPCWriteBlock name="Node_{@ID}_TZ" unit="{@TUnits}" Gain="1" location="{@TZOffset}" isCTRL = "false" />
		  </xsl:if>		  
        </xsl:for-each>          
     
    </xPC>

  </xsl:template>
</xsl:stylesheet>