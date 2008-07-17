<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" />

  <xsl:template match="/">

      <channelSet>
        <xsl:for-each select="//NeesDaqConfiguration/ChannelParameter">
          <xsl:if test="@InTest=1">
            <channel type="int" name="{@Name}" unit="{@Units}" Gain="{@Gain}" VoltageSlope="{@VoltageSlope}" VoltageOffset="{@VoltageOffset}" EUSlope="{@Sensitivity}" EUOffset="{@Offset}">
              
              <xsl:variable name="id" select="@Channel" />
              <xsl:variable name="location" select="//NEESSim/Scramnet/DaqBlock[@ChannelID=$id]/@Offset" />
  
              <xsl:attribute name="location">
                <xsl:choose>
                  <xsl:when test="$location &gt; 0">
                    <!-- <xsl:value-of select="(($location - 6291456)+512) div 4" /> -->
                    <xsl:value-of select="$location" /> 
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="-1" />
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
            </channel>
          </xsl:if>
        </xsl:for-each>
      </channelSet>

  </xsl:template>
</xsl:stylesheet>


