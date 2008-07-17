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
     
    </xPC>

  </xsl:template>
</xsl:stylesheet>


