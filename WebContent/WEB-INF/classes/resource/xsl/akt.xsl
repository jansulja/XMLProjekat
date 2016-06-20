<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:akt="http://www.parlament.gov.rs/akt"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    version="2.0">

    <xsl:output indent="yes" method="html" use-character-maps="no-control-characters"/>

    <xsl:character-map
        name="no-control-characters">
        <xsl:output-character character="&#127;" string=" "/>
        <xsl:output-character character="&#128;" string=" "/>
        <xsl:output-character character="&#129;" string=" "/>
        <xsl:output-character character="&#130;" string=" "/>
        <xsl:output-character character="&#131;" string=" "/>
        <xsl:output-character character="&#132;" string=" "/>
        <xsl:output-character character="&#133;" string=" "/>
        <xsl:output-character character="&#134;" string=" "/>
        <xsl:output-character character="&#135;" string=" "/>
        <xsl:output-character character="&#136;" string=" "/>
        <xsl:output-character character="&#137;" string=" "/>
        <xsl:output-character character="&#138;" string=" "/>
        <xsl:output-character character="&#139;" string=" "/>
        <xsl:output-character character="&#140;" string=" "/>
        <xsl:output-character character="&#141;" string=" "/>
        <xsl:output-character character="&#142;" string=" "/>
        <xsl:output-character character="&#143;" string=" "/>
        <xsl:output-character character="&#144;" string=" "/>
        <xsl:output-character character="&#145;" string=" "/>
        <xsl:output-character character="&#146;" string=" "/>
        <xsl:output-character character="&#147;" string=" "/>
        <xsl:output-character character="&#148;" string=" "/>
        <xsl:output-character character="&#149;" string=" "/>
        <xsl:output-character character="&#150;" string=" "/>
        <xsl:output-character character="&#151;" string=" "/>
        <xsl:output-character character="&#152;" string=" "/>
        <xsl:output-character character="&#153;" string=" "/>
        <xsl:output-character character="&#154;" string=" "/>
        <xsl:output-character character="&#155;" string=" "/>
        <xsl:output-character character="&#156;" string=" "/>
        <xsl:output-character character="&#157;" string=" "/>
        <xsl:output-character character="&#158;" string=" "/>
        <xsl:output-character character="&#159;" string=" "/>
    </xsl:character-map>

    <xsl:template match="/akt:Akt">
        <html>
            <head>

            </head>

            <body style="font-family:Helvetica">
                <br/>
                <h1 align="center" style="font-size:16pt; margin-top:93pt" ><xsl:value-of select="akt:Naziv"/></h1>

                <xsl:apply-templates/>

            </body>
        </html>
    </xsl:template>

    <xsl:template match="ds:Signature">
    </xsl:template>
    <xsl:template match="akt:ID"></xsl:template>
    <xsl:template match="akt:Status"></xsl:template>
    <xsl:template match="akt:Odbornik"></xsl:template>
    <xsl:template match="akt:Datum_predlaganja"></xsl:template>
    <xsl:template match="akt:Naziv"></xsl:template>

    <xsl:template match="akt:Deo">

        <h2 align="center" style="font-size:12.2pt; margin-top:23pt"><xsl:value-of select="akt:Naziv"/></h2>
        <xsl:apply-templates select="akt:Glava"/>
    </xsl:template>

    <xsl:template match="akt:Glava">

        <h2 align="center" style="font-size:12.2pt; margin-top:15pt"><xsl:value-of select="@redni_broj"/></h2>
        <h2 align="center" style="font-size:11.3pt"><xsl:value-of select="akt:Naziv"/></h2>
        <xsl:apply-templates select="akt:Odeljak"/>
        <xsl:apply-templates select="akt:Clan"/>
    </xsl:template>

    <xsl:template match="akt:Odeljak">


        <h2 align="center" style="font-size:11.3pt"><xsl:value-of select="akt:Naziv"/></h2>
        <xsl:apply-templates select="akt:Clan"/>
    </xsl:template>

    <xsl:template match="akt:Clan">

        <h2 align="center" style="font-size:11.3pt"><xsl:value-of select="akt:Naziv"/></h2>
        <h2 align="center" style="font-size:10.4pt">Član <xsl:value-of select="@redni_broj"/>.</h2>
        <xsl:apply-templates select="akt:Stav"/>

    </xsl:template>

    <xsl:template match="akt:Stav">
        <div align="center">
            <p align="justify" style="width: 400pt; text-indent: 80pt; font-size:10.4pt"><xsl:value-of select="akt:Tekst"/></p>
            <xsl:apply-templates select="akt:Tacka"/>
        </div>

    </xsl:template>

    <xsl:template match="akt:Tacka">

        <p align="justify" style="width: 400pt; text-indent: 80pt; font-size:10.4pt">
            <span>(<xsl:value-of select="@redni_broj"/>)</span>
            <xsl:value-of select="akt:Tekst"/>
        </p>
        <xsl:apply-templates select="akt:Podtacka"/>

    </xsl:template>

    <xsl:template match="akt:Podtacka">

        <p align="justify" style="width: 400pt; text-indent: 80pt; font-size:10.4pt">
            <span><xsl:value-of select="@redni_broj"/>)</span>
            <xsl:value-of select="akt:Tekst"/>
        </p>

    </xsl:template>




</xsl:stylesheet>
