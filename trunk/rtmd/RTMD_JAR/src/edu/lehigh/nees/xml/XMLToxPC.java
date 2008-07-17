package edu.lehigh.nees.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.JOptionPane;

import edu.lehigh.nees.util.FileHandler;


/********************************* 
 * XMLToxPC
 * <p>
 * Convert RTMD XML files that include SCRAMNet addresses to 
 * an XML file that can be converted to xPC Model and M files.
 * <p>
 * @author Thomas Marullo
 * <p>
 * <b><u>Revisions</u></b><br>
 *  8 Mar 06  T. Marullo  Initial
 * 27 Apr 06  T. Marullo  Added Pulse Extender as a static object
 * 27 Oct 06  T. Marullo  Added Units to SCR Read and Write object names
 * 28 Mar 07  T. Marullo  Added Integer support for Global Counter, Pulse Extender and Camera Trigger
 * 						  Automatic creation of Camera Trigger writer based on sample rate
 * 18 Jul 07  T. Marullo  Added _i and _o for blocks so names don't repeat and cause line connection issues
 * 						  Set camera trigger sample to 8Hz
 *  8 Aug 07  T. Marullo  Added LowerLimit and UpperLimit to Control Blocks for xPC
 *  7 Feb 08  T. Marullo  Updated to Simulink 7.0 (R2007b)
 * 
 ********************************/
public class XMLToxPC {

	/** Constructor: Create a new XMLToxPC Object */
	public XMLToxPC() {	
	}

    /** Convert to MDL and M files */
    public void convertToxPCFiles(String modelname, XMLxPCConfig xml, String path) {
    	try {
    		// Open mdl filestream
    		FileOutputStream mdlfile = null;        
    		mdlfile = new FileOutputStream(new String(path + "/" + modelname + ".mdl"));        
            PrintStream mdl = new PrintStream(mdlfile);
    		// Open m filestream
    		FileOutputStream mfile = null;        
    		mfile = new FileOutputStream(new String(path + "/" + modelname + "_setup.m"));        
            PrintStream m = new PrintStream(mfile);
            
            // Create Model File
            MDLTemplate mdloutput = new MDLTemplate(modelname);
            mdl.println(mdloutput.generateMDL(xml));
            
            // Create M File
            MTemplate moutput = new MTemplate(modelname);
            m.println(moutput.generateM(xml));            
            
            mdl.close();
            m.close();
    	} catch (Exception e) {e.printStackTrace();}
    }    
    
    public static void main (String argv []){                        
		// Get the input file name
    	String xmlFileName;
        if ((xmlFileName = FileHandler.getFilePath("Open xPC XML File")) == null)
        	System.exit(1);
        
    	// Get the output file name
    	String modelName;
        if ((modelName = JOptionPane.showInputDialog(null,"Enter Model name","modelname")).equals(""))        	
        	System.exit(1);
        
        // Convert to xPC Model files
        ReadxPCXMLConfig xml = new ReadxPCXMLConfig(new File(xmlFileName));
        XMLToxPC xmltoxpc = new XMLToxPC();
    	System.out.println("Converting " + xmlFileName + " to " + modelName + ".mdl and " + modelName + "_setup.m");
    	xmltoxpc.convertToxPCFiles(modelName, xml.getxPCConfig(), new File(xmlFileName).getParent());
    	System.out.println("Done");
    	
    	System.exit(0);
    }
}

/** Template for the Simulink Model File */
class MDLTemplate {
	private String mdlname;
	private String body;
	
	/** Create a new MDLTemplate */
	public MDLTemplate(String modelname) {
		// store name of model
		mdlname = modelname;	
		// create body of file String
		body = new String();			
	}
	
	/** Generate the text */
	public String generateMDL(XMLxPCConfig xml) {
		// Head of .mdl file
		body = 	"Model { \n" + 
				"  Name \"" + mdlname + "\"\n" +
				"  Version		  7.0\n" +
				"  MdlSubVersion		  0\n" +  
				"  GraphicalInterface { \n" +
				"    NumRootInports	    0 \n" +
				"    NumRootOutports	    0 \n" +
				"    ParameterArgumentNames  \"\" \n" +
				"    ComputedModelVersion    \"1.1\"\n" +
				"    NumModelReferences	    0\n" +
				"    NumTestPointedSignals   0\n" +
				"   }\n" +
				"  SavedCharacterEncoding  \"windows-1252\"\n" +
				"  InitFcn		\"" + mdlname + "_setup\"\n" +
				"  SaveDefaultBlockParams  on\n" +
				"  SampleTimeColors	  off\n" +
				"  LibraryLinkDisplay	  \"none\"\n" +
				"  WideLines		  off\n" +
				"  ShowLineDimensions	  off\n" +
				"  ShowPortDataTypes	  off\n" +
				"  ShowLoopsOnError	  on\n" +
				"  IgnoreBidirectionalLines off\n" +
				"  ShowStorageClass	  off\n" +
				"  ShowTestPointIcons	  on\n" +
				"  ShowViewerIcons	  on\n" +
				"  SortedOrder		  off\n" +
				"  ExecutionContextIcon	  off\n" +
				"  ShowLinearizationAnnotations on\n" +
				"  RecordCoverage	  off\n" +
				"  CovPath		  \"/\"\n" +
				"  CovSaveName		  \"covdata\"\n" +
				"  CovMetricSettings	  \"dw\"\n" +
				"  CovNameIncrementing	  off\n" +
				"  CovHtmlReporting	  on\n" +
				"  covSaveCumulativeToWorkspaceVar on\n" +
				"  CovSaveSingleToWorkspaceVar on\n" +
				"  CovCumulativeVarName	  \"covCumulativeData\"\n" +
				"  CovCumulativeReport	  off\n" +
				"  CovReportOnPause	  on\n" +
				"  ScopeRefreshTime	  0.035000\n" +
				"  OverrideScopeRefreshTime on\n" +
				"  DisableAllScopes	  off\n" +
				"  DataTypeOverride	  \"UseLocalSettings\"\n" +
				"  MinMaxOverflowLogging	  \"ForceOff\"\n" +
				"  MinMaxOverflowArchiveMode \"Overwrite\"\n" +
				"  BlockNameDataTip	  off\n" +
				"  BlockParametersDataTip  off\n" +
				"  BlockDescriptionStringDataTip	off\n" +
				"  ToolBar		  on\n" +
				"  StatusBar		  on\n" +
				"  BrowserShowLibraryLinks off\n" +
				"  BrowserLookUnderMasks	  off\n" +
				"  CloseFcn		  \"rtwprivate ssgencode ModelCloseRequest hybriddaq1do\"\n" +
				"\"f\"\n" +
				"  Created		  \"Mon Jan 01 12:00:00 2006\"\n" +
				"  UpdateHistory		  \"UpdateHistoryNever\"\n" +
				"  ModifiedByFormat	  \"%<Auto>\"\n" +
				"  LastModifiedBy	  \"NEESsim1\"\n" +
				"  ModifiedDateFormat	  \"%<Auto>\"\n" +
				"  LastModifiedDate	  \"Mon Jan 01 12:00:01 2006\"\n" +
				"  ModelVersionFormat	  \"1.%<AutoIncrement:148>\"\n" +
				"  ConfigurationManager	  \"None\"\n" +
				"  LinearizationMsg	  \"none\"\n" +
				"  Profile		  off\n" +
				"  ParamWorkspaceSource	  \"MATLABWorkspace\"\n" +
				"  AccelSystemTargetFile	  \"accel.tlc\"\n" +
				"  AccelTemplateMakefile	  \"accel_default_tmf\"\n" +
				"  AccelMakeCommand	  \"make_rtw\"\n" +
				"  TryForcingSFcnDF	  off\n" +
				"  ExtModeBatchMode	  off\n" +
				"  ExtModeEnableFloating	  on\n" +
				"  ExtModeTrigType	  \"manual\"\n" +
				"  ExtModeTrigMode	  \"normal\"\n" +
				"  ExtModeTrigPort	  \"1\"\n" +
				"  ExtModeTrigElement	  \"any\"\n" +
				"  ExtModeTrigDuration	  1000\n" +
				"  ExtModeTrigDurationFloating \"auto\"\n" +
				"  ExtModeTrigHoldOff	  0\n" +
				"  ExtModeTrigDelay	  0\n" +
				"  ExtModeTrigDirection	  \"rising\"\n" +
				"  ExtModeTrigLevel	  0\n" +
				"  ExtModeArchiveMode	  \"off\"\n" +
				"  ExtModeAutoIncOneShot	  off\n" +
				"  ExtModeIncDirWhenArm	  off\n" +
				"  ExtModeAddSuffixToVar	  off\n" +
				"  ExtModeWriteAllDataToWs off\n" +
				"  ExtModeArmWhenConnect	  on\n" +
				"  ExtModeSkipDownloadWhenConnect off\n" +
				"  ExtModeLogAll		  on\n" +
				"  ExtModeAutoUpdateStatusClock on\n" +
				"  BufferReuse		  off\n" +
				"  StrictBusMsg		  \"ErrorLevel1\"\n" +
				"  ProdHWDeviceType	  \"32-bit Generic\"\n" +
				"  ShowModelReferenceBlockVersion off\n" +
				"  ShowModelReferenceBlockIO off\n" +
				"  Array {\n" +
				"    Type		    \"Handle\"\n" +
				"    Dimension		    1\n" +
				"    Simulink.ConfigSet {\n" +
				"      $ObjectID		      1\n" +
				"      Version		      \"1.1.0\"\n" +
				"      Array {\n" +
				"	Type			\"Handle\"\n" +
				"	Dimension		7\n" +
				"	Simulink.SolverCC {\n" +
				"	  $ObjectID		  2\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  StartTime		  \"0.0\"\n" +
				"	  StopTime		  \"stime\"\n" +
				"	  AbsTol		  \"auto\"\n" +
				"	  FixedStep		  \"1/1024\"\n" +
				"	  InitialStep		  \"auto\"\n" +
				"	  MaxNumMinSteps	  \"-1\"\n" +
				"	  MaxOrder		  5\n" +
				"	  ExtrapolationOrder	  4\n" +
				"	  NumberNewtonIterations  1\n" +
				"	  MaxStep		  \"auto\"\n" +
				"	  MinStep		  \"auto\"\n" +
				"	  RelTol		  \"1e-3\"\n" +
				"	  SolverMode		  \"SingleTasking\"\n" +
				"	  Solver		  \"FixedStepDiscrete\"\n" +
				"	  SolverName		  \"FixedStepDiscrete\"\n" +
				"	  ZeroCrossControl	  \"UseLocalSettings\"\n" +
				"	  AlgebraicLoopSolver	  \"TrustRegion\"\n" +
				"	  SolverResetMethod	  \"Fast\"\n" +
				"	  PositivePriorityOrder	  off\n" +
				"	  AutoInsertRateTranBlk	  off\n" +
				"	  SampleTimeConstraint	  \"Unconstrained\"\n" +
				"	  RateTranMode		  \"Deterministic\"\n" +
				"	}\n" +
				"	Simulink.DataIOCC {\n" +
				"	  $ObjectID		  3\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  Decimation		  \"1\"\n" +
				"	  ExternalInput		  \"[t, u]\"\n" +
				"	  FinalStateName	  \"xFinal\"\n" +
				"	  InitialState		  \"xInitial\"\n" +
				"	  LimitDataPoints	  on\n" +
				"	  MaxDataPoints		  \"1000\"\n" +
				"	  LoadExternalInput	  off\n" +
				"	  LoadInitialState	  off\n" +
				"	  SaveFinalState	  off\n" +
				"	  SaveFormat		  \"Array\"\n" +
				"	  SaveOutput		  on\n" +
				"	  SaveState		  off\n" +
				"	  SignalLogging		  on\n" +
				"	  InspectSignalLogs	  off\n" +
				"	  SaveTime		  on\n" +
				"	  StateSaveName		  \"xout\"\n" +
				"	  TimeSaveName		  \"tout\"\n" +
				"	  OutputSaveName	  \"yout\"\n" +
				"	  SignalLoggingName	  \"logsout\"\n" +
				"	  OutputOption		  \"RefineOutputTimes\"\n" +
				"	  OutputTimes		  \"[]\"\n" +
				"	  Refine		  \"1\"\n" +
				"	}\n" +
				"	Simulink.OptimizationCC {\n" +
				"	  $ObjectID		  4\n" +
				"	  Array {\n" +
				"	    Type		    \"Cell\"\n" +
				"	    Dimension		    5\n" +
				"	    Cell		    \"ZeroExternalMemoryAtStartup\"\n" +
				"	    Cell		    \"ZeroInternalMemoryAtStartup\"\n" +
				"	    Cell		    \"InitFltsAndDblsToZero\"\n" +
				"	    Cell		    \"OptimizeModelRefInitCode\"\n" +
				"	    Cell		    \"NoFixptDivByZeroProtection\"\n" +
				"	    PropName		    \"DisabledProps\"\n" +
				"	  }\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  BlockReduction	  off\n" +
				"	  BooleanDataType	  on\n" +
				"	  ConditionallyExecuteInputs on\n" +
				"	  InlineParams		  off\n" +
				"	  InlineInvariantSignals  off\n" +
				"	  OptimizeBlockIOStorage  off\n" +
				"	  BufferReuse		  off\n" +
				"	  EnforceIntegerDowncast  on\n" +
				"	  ExpressionFolding	  off\n" +
				"     ExpressionDepthLimit	  2147483647\n" +
				"	  FoldNonRolledExpr	  on\n" +
				"	  LocalBlockOutputs	  off\n" +
				"	  ParameterPooling	  on\n" +
				"	  RollThreshold		  5\n" +
				"	  SystemCodeInlineAuto	  off\n" +
				"	  StateBitsets		  off\n" +
				"	  DataBitsets		  off\n" +
				"	  UseTempVars		  off\n" +
				"	  ZeroExternalMemoryAtStartup on\n" +
				"	  ZeroInternalMemoryAtStartup on\n" +
				"	  InitFltsAndDblsToZero	  on\n" +
				"	  NoFixptDivByZeroProtection off\n" +
				"	  EfficientFloat2IntCast  off\n" +
				"	  OptimizeModelRefInitCode off\n" +
				"	  LifeSpan		  \"inf\"\n" +
				"	  BufferReusableBoundary  on\n" +
				"     SimCompilerOptimization \"Off\"\n"+
				"     AccelVerboseBuild	  off\n" +
 				"	}\n" +
				"	Simulink.DebuggingCC {\n" +
				"	  $ObjectID		  5\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  RTPrefix		  \"error\"\n" +
				"	  ConsistencyChecking	  \"none\"\n" +
				"	  ArrayBoundsChecking	  \"none\"\n" +
				"	  SignalInfNanChecking	  \"none\"\n" +
				"	  ReadBeforeWriteMsg	  \"UseLocalSettings\"\n" +
				"	  WriteAfterWriteMsg	  \"UseLocalSettings\"\n" +
				"	  WriteAfterReadMsg	  \"UseLocalSettings\"\n" +
				"	  AlgebraicLoopMsg	  \"warning\"\n" +
				"	  ArtificialAlgebraicLoopMsg \"warning\"\n" +
				"	  CheckSSInitialOutputMsg on\n" +
				"	  CheckExecutionContextPreStartOutputMsg off\n" +
				"	  CheckExecutionContextRuntimeOutputMsg	off\n" +
				"	  SignalResolutionControl \"TryResolveAllWithWarning\"\n" +
				"	  BlockPriorityViolationMsg \"warning\"\n" +
				"	  MinStepSizeMsg	  \"warning\"\n" +
				"	  SolverPrmCheckMsg	  \"warning\"\n" +
				"	  InheritedTsInSrcMsg	  \"warning\"\n" +
				"	  DiscreteInheritContinuousMsg \"warning\"\n" +
				"	  MultiTaskDSMMsg	  \"warning\"\n" +
				"	  MultiTaskRateTransMsg	  \"error\"\n" +
				"	  SingleTaskRateTransMsg  \"none\"\n" +
				"	  TasksWithSamePriorityMsg \"warning\"\n" +
				"	  SigSpecEnsureSampleTimeMsg \"warning\"\n" +
				"	  CheckMatrixSingularityMsg \"none\"\n" +
				"	  IntegerOverflowMsg	  \"warning\"\n" +
				"	  Int32ToFloatConvMsg	  \"warning\"\n" +
				"	  ParameterDowncastMsg	  \"error\"\n" +
				"	  ParameterOverflowMsg	  \"error\"\n" +
				"	  ParameterUnderflowMsg	  \"none\"\n" +
				"	  ParameterPrecisionLossMsg \"warning\"\n" +
				"	  UnderSpecifiedDataTypeMsg \"none\"\n" +
				"	  UnnecessaryDatatypeConvMsg \"none\"\n" +
				"	  VectorMatrixConversionMsg \"none\"\n" +
				"	  InvalidFcnCallConnMsg	  \"error\"\n" +
				"	  FcnCallInpInsideContextMsg \"Use local settings\"\n" +
				"	  SignalLabelMismatchMsg  \"none\"\n" +
				"	  UnconnectedInputMsg	  \"warning\"\n" +
				"	  UnconnectedOutputMsg	  \"warning\"\n" +
				"	  UnconnectedLineMsg	  \"warning\"\n" +
				"	  SFcnCompatibilityMsg	  \"none\"\n" +
				"	  UniqueDataStoreMsg	  \"none\"\n" +
				"	  BusObjectLabelMismatch  \"warning\"\n" +
				"	  RootOutportRequireBusObject \"warning\"\n" +
				"	  AssertControl		  \"UseLocalSettings\"\n" +
				"	  EnableOverflowDetection off\n" +
				"	  ModelReferenceIOMsg	  \"none\"\n" +
				"	  ModelReferenceVersionMismatchMessage \"none\"\n" +
				"	  ModelReferenceIOMismatchMessage \"none\"\n" +
				"	  ModelReferenceCSMismatchMessage \"none\"\n" +
				"	  ModelReferenceSimTargetVerbose off\n" +
				"	  UnknownTsInhSupMsg	  \"warning\"\n" +
				"	  ModelReferenceDataLoggingMessage \"warning\"\n" +
				"	  ModelReferenceSymbolNameMessage \"warning\"\n" +
				"	  ModelReferenceExtraNoncontSigs \"error\"\n" +
				"	}\n" +
				"	Simulink.HardwareCC {\n" +
				"	  $ObjectID		  6\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  ProdBitPerChar	  8\n" +
				"	  ProdBitPerShort	  16\n" +
				"	  ProdBitPerInt		  32\n" +
				"	  ProdBitPerLong	  32\n" +
				"	  ProdIntDivRoundTo	  \"Undefined\"\n" +
				"	  ProdEndianess		  \"Unspecified\"\n" +
				"	  ProdWordSize		  32\n" +
				"	  ProdShiftRightIntArith  on\n" +
				"	  ProdHWDeviceType	  \"32-bit Generic\"\n" +
				"	  TargetBitPerChar	  8\n" +
				"	  TargetBitPerShort	  16\n" +
				"	  TargetBitPerInt	  32\n" +
				"	  TargetBitPerLong	  32\n" +
				"	  TargetShiftRightIntArith on\n" +
				"	  TargetIntDivRoundTo	  \"Undefined\"\n" +
				"	  TargetEndianess	  \"Unspecified\"\n" +
				"	  TargetWordSize	  32\n" +
				"	  TargetTypeEmulationWarnSuppressLevel 0\n" +
				"	  TargetPreprocMaxBitsSint 32\n" +
				"	  TargetPreprocMaxBitsUint 32\n" +
				"	  TargetHWDeviceType	  \"Specified\"\n" +
				"	  TargetUnknown		  off\n" +
				"	  ProdEqTarget		  on\n" +
				"	}\n" +
				"	Simulink.ModelReferenceCC {\n" +
				"	  $ObjectID		  7\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  UpdateModelReferenceTargets \"IfOutOfDateOrStructuralChange\"\n" +
				"	  CheckModelReferenceTargetMessage \"error\"\n" +
				"	  ModelReferenceNumInstancesAllowed \"Multi\"\n" +
				"	  ModelReferencePassRootInputsByReference on\n" +
				"	  ModelReferenceMinAlgLoopOccurrences off\n" +
				"	}\n" +
				"	Simulink.RTWCC {\n" +
				"	  $BackupClass		  \"Simulink.RTWCC\"\n" +
				"	  $ObjectID		  8\n" +
				"	  Array {\n" +
				"	    Type		    \"Cell\"\n" +
				"	    Dimension		    2\n" +
				"	    Cell		    \"IncludeHyperlinkInReport\"\n" +
				"       Cell		    \"GenerateTraceInfo\"\n" +
				"	    PropName		    \"DisabledProps\"\n" +
				"	  }\n" +
				"	  Version		  \"1.3.0\"\n" +
				"	  SystemTargetFile	  \"xpctarget.tlc\"\n" +
				"	  GenCodeOnly		  off\n" +
				"	  MakeCommand		  \"make_rtw\"\n" +
				"	  TemplateMakefile	  \"xpc_default_tmf\"\n" +
				"	  Description		  \"xPC Target\"\n" +
				"	  GenerateReport	  off\n" +
				"	  SaveLog		  off\n" +
				"	  RTWVerbose		  on\n" +
				"	  RetainRTWFile		  off\n" +
				"	  ProfileTLC		  off\n" +
				"	  TLCDebug		  off\n" +
				"	  TLCCoverage		  off\n" +
				"	  TLCAssert		  off\n" +
				"	  ProcessScriptMode	  \"Default\"\n" +
				"	  ConfigurationMode	  \"Optimized\"\n" +
				"	  ProcessScript		  \"xpctarget_make_rtw_hook\"\n" +
				"	  ConfigAtBuild		  off\n" +
				"	  IncludeHyperlinkInReport off\n" +
				"	  LaunchReport		  off\n" +
				"	  TargetLang		  \"C\"\n" +
				"	  Array {\n" +
				"	    Type		    \"Handle\"\n" +
				"	    Dimension		    2\n" +
				"	    Simulink.CodeAppCC {\n" +
				"	      $ObjectID		      9\n" +
				"	      Array {\n" +
				"		Type			\"Cell\"\n" +
				"		Dimension		9\n" +
				"		Cell			\"IgnoreCustomStorageClasses\"\n" +
				"		Cell			\"InsertBlockDesc\"\n" +
				"		Cell			\"SFDataObjDesc\"\n" +
				"		Cell			\"SimulinkDataObjDesc\"\n" +
				"		Cell			\"DefineNamingRule\"\n" +
				"		Cell			\"SignalNamingRule\"\n" +
				"		Cell			\"ParamNamingRule\"\n" +
				"		Cell			\"InlinedPrmAccess\"\n" +
				"		Cell			\"CustomSymbolStr\"\n" +
				"		PropName		\"DisabledProps\"\n" +
				"	      }\n" +
				"	      Version		      \"1.3.0\"\n" +
				"	      ForceParamTrailComments off\n" +
				"	      GenerateComments	      on\n" +
				"	      IgnoreCustomStorageClasses on\n" +
				"	      IncHierarchyInIds	      off\n" +
				"	      MaxIdLength	      31\n" +
				"	      PreserveName	      off\n" +
				"	      PreserveNameWithParent  off\n" +
				"	      ShowEliminatedStatement off\n" +
				"	      IncAutoGenComments      off\n" +
				"	      SimulinkDataObjDesc     off\n" +
				"	      SFDataObjDesc	      off\n" +
				"	      IncDataTypeInIds	      off\n" +
				"	      PrefixModelToSubsysFcnNames on\n" +
				"	      CustomSymbolStr	      \"$R$N$M\"\n" +
				"	      MangleLength	      1\n" +
				"	      DefineNamingRule	      \"None\"\n" +
				"	      ParamNamingRule	      \"None\"\n" +
				"	      SignalNamingRule	      \"None\"\n" +
				"	      InsertBlockDesc	      off\n" +
				"	      SimulinkBlockComments   on\n" +
				"	      EnableCustomComments    off\n" +
				"	      InlinedPrmAccess	      \"Literals\"\n" +
				"	      ReqsInCode	      off\n" +
				"	    }\n" +
				"	    xpctarget.xpcTargetCC {\n" +
				"	      $BackupClass	      \"Simulink.TargetCC\"\n" +
				"	      $ObjectID		      10\n" +
				"	      Array {\n" +
				"		Type			\"Cell\"\n" +
				"		Dimension		15\n" +
				"		Cell			\"IncludeMdlTerminateFcn\"\n" +
				"		Cell			\"CombineOutputUpdateFcns\"\n" +
				"		Cell			\"SuppressErrorStatus\"\n" +
				"		Cell			\"ERTCustomFileBanners\"\n" +
				"		Cell			\"GenerateSampleERTMain\"\n" +
				"       Cell			\"GenerateTestInterfaces\"\n" +
				"       Cell			\"ModelStepFunctionPrototypeControlCompliant\"\n" +
				"		Cell			\"MultiInstanceERTCode\"\n" +
				"		Cell			\"PurelyIntegerCode\"\n" +
				"		Cell			\"SupportNonFinite\"\n" +
				"		Cell			\"SupportComplex\"\n" +
				"		Cell			\"SupportAbsoluteTime\"\n" +
				"		Cell			\"SupportContinuousTime\"\n" +
				"		Cell			\"SupportNonInlinedSFcns\"\n" +
				"       Cell			\"PortableWordSizes\"\n" +
				"		PropName		\"DisabledProps\"\n" +
				"	      }\n" +
				"	      Version		      \"1.3.0\"\n" +
				"	      TargetFcnLib	      \"ansi_tfl_tmw.mat\"\n" +
				"	      TargetLibSuffix	      \"\"\n" +
				"	      TargetPreCompLibLocation \"\"\n" +
				"	      GenFloatMathFcnCalls    \"ANSI_C\"\n" +
				"	      UtilityFuncGeneration   \"Auto\"\n" +
				"	      GenerateFullHeader      on\n" +
				"	      GenerateSampleERTMain   off\n" +
				"	      IsPILTarget	      off\n" +
				"	      ModelReferenceCompliant on\n" +
				"	      IncludeMdlTerminateFcn  on\n" +
				"	      CombineOutputUpdateFcns off\n" +
				"	      SuppressErrorStatus     off\n" +
				"	      IncludeFileDelimiter    \"Auto\"\n" +
				"	      ERTCustomFileBanners    off\n" +
				"	      SupportAbsoluteTime     on\n" +
				"	      LogVarNameModifier      \"rt_\"\n" +
				"	      MatFileLogging	      on\n" +
				"	      MultiInstanceERTCode    off\n" +
				"	      SupportNonFinite	      on\n" +
				"	      SupportComplex	      on\n" +
				"	      PurelyIntegerCode	      off\n" +
				"	      SupportContinuousTime   on\n" +
				"	      SupportNonInlinedSFcns  on\n" +
				"	      RL32ModeModifier	      \"Real-Time\"\n" +
				"	      ExtMode		      on\n" +
				"	      ExtModeMexFile	      \"ext_xpc\"\n" +
				"	      ExtModeArmWhenConnect   off\n" +
				"	      RL32LogTETModifier      on\n" +
				"	      RL32LogBufSizeModifier  \"100000\"\n" +
				"	      RL32IRQSourceModifier   \"Timer\"\n" +
				"	      xPCIRQSourceBoard	      \"None/Other\"\n" +
				"	      xPCIOIRQSlot	      \"-1\"\n" +
				"	      xpcDblBuff	      off\n" +
				"	      xpcObjCom		      off\n" +
				"	      xPCGenerateASAP2	      off\n" +
				"	      RL32ObjectName	      \"tg\"\n" +
				"	      xPCisDownloadable	      on\n" +
				"	      xPCisDefaultEnv	      on\n" +
				"	    }\n" +
				"	    PropName		    \"Components\"\n" +
				"	  }\n" +
				"	}\n" +
				"	PropName		\"Components\"\n" +
				"      }\n" +
				"      Name		      \"Configuration\"\n" +
				"      SimulationMode	      \"normal\"\n" +
				"      CurrentDlgPage	      \"Real-Time Workshop\"\n" +
				"    }\n" +
				"    PropName		    \"ConfigurationSets\"\n" +
				"  }\n" +
				"  Simulink.ConfigSet {\n" +
				"    $PropName		    \"ActiveConfigurationSet\"\n" +
				"    $ObjectID		    1\n" +
				"  }\n" +
				"  BlockDefaults {\n" +
				"    Orientation		    \"right\"\n" +
				"    ForegroundColor	    \"black\"\n" +
				"    BackgroundColor	    \"white\"\n" +
				"    DropShadow		    off\n" +
				"    NamePlacement	    \"normal\"\n" +
				"    FontName		    \"arial\"\n" +
				"    FontSize		    10\n" +
				"    FontWeight		    \"normal\"\n" +
				"    FontAngle		    \"normal\"\n" +
				"    ShowName		    on\n" +
				"  }\n" +
				"  BlockParameterDefaults {\n" +
				"   Block {\n" +
				"     BlockType		      Constant\n" +
				"     Value		      \"1\"\n" +
				"     VectorParams1D	      on\n" +
				"     OutDataTypeMode	      \"Inherit from 'Constant value'\"\n" +
				"     OutDataType	      \"sfix(16)\"\n" +
				"     ConRadixGroup	      \"Use specified scaling\"\n" +
				"     OutScaling	      \"2^0\"\n" +
				"     SampleTime	      \"sample\"\n" +
				"    }\n" +
				"    Block {\n" +
				"      BlockType		      BusCreator\n" +
				"      Inputs		      \"4\"\n" +
				"      DisplayOption	      \"none\"\n" +
				"      UseBusObject	      off\n" +
				"      BusObject		      \"BusObject\"\n" +
				"      NonVirtualBus	      off\n" +
				"    }\n" +
				"    Block {\n" +
				"      BlockType		      Gain\n" +
				"      Gain		      \"1\"\n" +
				"      Multiplication	      \"Element-wise(K.*u)\"\n" +
				"      ParameterDataTypeMode   \"Same as input\"\n" +
				"      ParameterDataType	      \"sfix(16)\"\n" +
				"      ParameterScalingMode    \"Best Precision: Matrix-wise\"\n" +
				"      ParameterScaling	      \"2^0\"\n" +
				"      OutDataTypeMode	      \"Same as input\"\n" +
				"      OutDataType	      \"sfix(16)\"\n" +
				"      OutScaling	      \"2^0\"\n" +
				"      LockScale		      off\n" +
				"      RndMeth		      \"Floor\"\n" +
				"      SaturateOnIntegerOverflow	on\n" +
				"      SampleTime	      \"-1\"\n" +
				"    }\n" +
				"    Block {\n" +
				"      BlockType		      Inport\n" +
				"      Port		      \"1\"\n" +
				"      UseBusObject	      off\n" +
				"      BusObject		      \"BusObject\"\n" +
				"      BusOutputAsStruct	      off\n" +
				"      PortDimensions	      \"-1\"\n" +
				"      SampleTime	      \"-1\"\n" +
				"      DataType		      \"auto\"\n" +
				"      OutDataType	      \"sfix(16)\"\n" +
				"      OutScaling	      \"2^0\"\n" +
				"      SignalType	      \"auto\"\n" +
				"      SamplingMode	      \"auto\"\n" +
				"      LatchByDelayingOutsideSignal off\n" +
				"      LatchByCopyingInsideSignal off\n" +
				"      Interpolate	      on\n" +
				"    }\n" +
				"    Block {\n" +
				"      BlockType		      \"S-Function\"\n" +
				"      FunctionName	      \"system\"\n" +
				"      SFunctionModules	      \"''\"\n" +
				"      PortCounts	      \"[]\"\n" +
				"    }\n" +
				"    Block {\n" +
				"      BlockType		      SubSystem\n" +
				"      ShowPortLabels	      on\n" +
				"      Permissions	      \"ReadWrite\"\n" +
				"      PermitHierarchicalResolution \"All\"\n" +
				"      SystemSampleTime	      \"-1\"\n" +
				"      RTWFcnNameOpts	      \"Auto\"\n" +
				"      RTWFileNameOpts	      \"Auto\"\n" +
				"      SimViewingDevice	      off\n" +
				"      DataTypeOverride	      \"UseLocalSettings\"\n" +
				"      MinMaxOverflowLogging   \"UseLocalSettings\"\n" +
				"    }\n" +
				"  }\n" +
				"  AnnotationDefaults {\n" +
				"    HorizontalAlignment	    \"center\"\n" +
				"    VerticalAlignment	    \"middle\"\n" +
				"    ForegroundColor	    \"black\"\n" +
				"    BackgroundColor	    \"white\"\n" +
				"    DropShadow		    off\n" +
				"    FontName		    \"arial\"\n" +
				"    FontSize		    10\n" +
				"    FontWeight		    \"normal\"\n" +
				"    FontAngle		    \"normal\"\n" +
				"  }\n" +
				"  LineDefaults {\n" +
				"    FontName		    \"arial\"\n" +
				"    FontSize		    9\n" +
				"    FontWeight		    \"normal\"\n" +
				"    FontAngle		    \"normal\"\n" +
				"  }\n" +
				"  System {\n" +
				"	    Name		    \"" + mdlname + "\"\n" +
				"	    Location		    [100, 100, 1000, 500]\n" +
				"	    Open		    on\n" +
				"	    ModelBrowserVisibility  off\n" +
				"	    ModelBrowserWidth	    200\n" +
				"	    ScreenColor		    \"white\"\n" +
				"	    PaperOrientation	    \"landscape\"\n" +
				"	    PaperPositionMode	    \"auto\"\n" +
				"	    PaperType		    \"usletter\"\n" +
				"	    PaperUnits		    \"inches\"\n" +
				"	    ZoomFactor		    \"100\"\n" +
				"	    ReportName		    \"simulink-default.rpt\"\n" + 
				"		Block {\n" + 
				"         BlockType		      Reference\n" + 
				"         Name		      \"SC150 init \"\n" + 
				"         Ports		      []\n" + 
				"         Position		      [0, 0, 30, 10]\n" + 
				"         SourceBlock	      \"xpclib/Shared\\nMemory/Systran/SC150 init \"\n" + 
				"         SourceType	      \"SC150 Init\"\n" + 
				"         node		      \"[node]\"\n" + 
				"         pci		      \"-1\"\n" + 
				"       }\n" +
				"       Block {\n" +
				"         BlockType		      Constant\n" +
				"         Name		      \"TriggerConstant\"\n" +
				"         ShowName		      off\n" +				
				"         Position		      [20, 20, 30, 30]\n" +
		    	"       }\n" +
		    	"       Block {\n" +
		    	"         BlockType		      Reference\n" +
		    	"         Name		      \"TriggerFPtoFix\"\n" +
		    	"         Ports		      [1, 1]\n" +
		    	"         Position		      [100, 20, 170, 30]\n" +
		    	"         BackgroundColor	      \"yellow\"\n" +
		    	"         ShowName		      off\n" +
		    	"         SourceBlock	      \"RTMDblocks/Convert FP to Fix\"\n" +
		    	"         SourceType	      \"FPtoFix\"\n" +
		    	"       }\n" +
		    	"       Block {\n" +
		    	"         BlockType		      Reference\n" +
		    	"         Name		      \"Pulse Extender Trigger\"\n" +
		    	"         Ports		      [1]\n" +
		    	"         Position		      [190, 20, 220, 30]\n" +
		    	"         BackgroundColor	      \"green\"\n" +
		    	"         SourceBlock	      \"xpclib/Shared\nMemory/Systran/SC150 write \"\n" +
		    	"         SourceType	      \"SC150 Write\"\n" +
		    	"         partition		      \"[ptrigger]\"\n" +
		    	"         ts		      \"sample\"\n" +
		    	"         pci		      \"-1\"\n" +
		    	"       }\n" +
		    	"      Line {\n" +
		    	"         SrcBlock		      \"TriggerConstant\"\n" +
		    	"         SrcPort		      1\n" +
		    	"         DstBlock		      \"TriggerFPtoFix\"\n" +
		    	"         DstPort		      1\n" +
		    	"       }\n" +
		    	"       Line {\n" +
		    	"         SrcBlock		      \"TriggerFPtoFix\"\n" +
		    	"         SrcPort		      1\n" +
		    	"         DstBlock		      \"Pulse Extender Trigger\"\n" +
		    	"         DstPort		      1\n" +
		    	"       }\n" + 
		    	"       Block {\n" +
				"         BlockType		      Reference\n" +
				"         Name		      \"CameraTriggerCounter\"\n" +
				"         Ports		      [0, 1]\n" +
				"         SourceBlock	      \"simulink/Sources/Counter\nLimited\"\n" +
				"         SourceType	      \"Counter Limited\"\n" +
				"         ShowName		      off\n" +				
				"         Position		      [300, 20, 330, 30]\n" +
				"         ShowPortLabels	      \"off\"\n" +			    
			    "         uplimit		      \"1000000\"\n" +
			    "         tsamp		          \"0.125\"\n" +
		    	"       }\n" +
		    	"       Block {\n" +
		    	"         BlockType		      DataTypeConversion\n" +
		    	"         Name		          \"CameraTriggerConversion\"\n" +		    	
		    	"         Position		      [350, 20, 380, 30]\n" +		    	
		    	"         ShowName		      off\n" +
		    	"         OutDataTypeMode	  \"uint32\"\n" + 
		    	"         RndMeth	          \"Floor\"\n" +
		    	"         SaturateOnIntegerOverflow	      \"off\"\n" +
		    	"       }\n" +
		    	"       Block {\n" +
		    	"         BlockType		      Reference\n" +
		    	"         Name		      \"CameraTrigger\"\n" +
		    	"         Ports		      [1]\n" +
		    	"         Position		      [410, 20, 440, 30]\n" +
		    	"         BackgroundColor	      \"green\"\n" +
		    	"         SourceBlock	      \"xpclib/Shared\nMemory/Systran/SC150 write \"\n" +
		    	"         SourceType	      \"SC150 Write\"\n" +
		    	"         partition		      \"[pcameratrigger]\"\n" +
		    	"         ts		      \"sample\"\n" +
		    	"         pci		      \"-1\"\n" +
		    	"       }\n" +
		    	"      Line {\n" +
		    	"         SrcBlock		      \"CameraTriggerCounter\"\n" +
		    	"         SrcPort		      1\n" +
		    	"         DstBlock		      \"CameraTriggerConversion\"\n" +
		    	"         DstPort		      1\n" +
		    	"       }\n" +
		    	"       Line {\n" +
		    	"         SrcBlock		      \"CameraTriggerConversion\"\n" +
		    	"         SrcPort		      1\n" +
		    	"         DstBlock		      \"CameraTrigger\"\n" +
		    	"         DstPort		      1\n" +
		    	"       }\n";
		    	
				
		// Generate Blocks 
		int id = 0;
		// Generate Write Blocks
		for (id = 0; id < xml.getnumxPCWriteBlocks(); id++) {
			// Ignore Pulse Trigger and Camera Trigger
			if (!xml.getxPCWriteLocation(id).equals("63") && !xml.getxPCWriteLocation(id).equals("61")) {
				body = body + generateInPort(id);
				body = body + generateGain(id, Double.toString(xml.getxPCWriteGain(id)));
				// Add limits
				if (xml.getxPCWriteisCTRL(id).equals("true"))
					body = body + generateLimitChecker(id, new String(replaceWhiteWithUnderscore(xml.getxPCWriteName(id)) + "_lowerlimit"), new String(replaceWhiteWithUnderscore(xml.getxPCWriteName(id)) + "_upperlimit"));
				body = body + generateFPtoFix(id);
				body = body + generateSCRWrite(id, new String("p" + id), xml.getxPCWriteName(id), xml.getxPCWriteUnits(id));
				// Add limits
				if (xml.getxPCWriteisCTRL(id).equals("true")) {
					body = body + generateLine(new String("In " + id), "1", new String("Limit Checker " + id), "1");
					body = body + generateLine(new String("Limit Checker " + id), "1", new String("Gain " + id), "1");
				}
				else {
					body = body + generateLine(new String("In " + id), "1", new String("Gain " + id), "1");					
				}
				body = body + generateLine(new String("Gain " + id), "1", new String("FP to Fix " + id), "1");
				body = body + generateLine(new String("FP to Fix " + id), "1", xml.getxPCWriteName(id) + " (" + xml.getxPCWriteUnits(id) + ")_o", "1");
			}
		}
		
		// Generate File and Bus blocks
		int numBlocks = xml.getnumxPCReadBlocks();		
		int fileid = 1;
		// Each File and Bus can take up to 10 Read blocks
		while (numBlocks > 0) {
			// Create a bus with the remaining read blocks
			if (numBlocks < 10)
				body = body + generateBus(fileid,Integer.toString(numBlocks),id + fileid-1);
			// Create a bus with 10 read blocks
			else
				body = body + generateBus(fileid,"10",id + fileid-1);			
			numBlocks-=10;
			// Create same amount of File blocks and connect to each Bus
			body = body + generateFileTarget(fileid, id + fileid-1);
			body = body + generateLine(new String("Bus " + fileid),"1",new String("XPC" + fileid),"1");
			fileid++;
		}
		
		// Generate Read Blocks		
		int idoffset = id;
		int busportid = 1;
		int busid = 1;		
		for (id = 0; id < xml.getnumxPCReadBlocks(); id++) {
			body = body + generateSCRRead(id + idoffset, new String("p" + (id + idoffset)), xml.getxPCReadName(id), xml.getxPCReadUnits(id));
			// Check if its a DAQ (16b Int) or Camera Trigger or Pulse Extender or Global Counter which are Ints
			if (xml.getxPCReadisDAQ(id).equals("true") || xml.getxPCReadLocation(id).equals("61") || xml.getxPCReadLocation(id).equals("63") || xml.getxPCReadLocation(id).equals("64")) {
				// DAQ
				body = body + generateConverter(id + idoffset);
				body = body + generateLine(xml.getxPCReadName(id) + " (" + xml.getxPCReadUnits(id) + ")_i","1",new String("Converter "+(id+idoffset)),"1");
				body = body + generateLine(new String("Converter "+(id+idoffset)),"1",new String("Bus " + busid),Integer.toString(busportid));
			}
			// or SIM (32b FP) 
			else {
				// SIM
				body = body + generateFixToFP(id + idoffset);
				body = body + generateLine(xml.getxPCReadName(id) + " (" + xml.getxPCReadUnits(id) + ")_i","1",new String("Fix to FP "+(id+idoffset)),"1");
				body = body + generateLine(new String("Fix to FP "+(id+idoffset)),"1",new String("Bus " + busid),Integer.toString(busportid));
			}			
			// Increment bus port and reset if all 10 per bus have been used and move on to next bus
			busportid++;			
			if (busportid == 11) {
				busportid = 1;
				busid++;
			}
		}
		
		// End of .mdl file
		body = body + 
				"  }\n" +
				"}\n" +
				"\n" +
				"# Finite State Machines\n" +
				"#\n" +
				"#    Stateflow Version 6.2 (R14SP2) dated Aug  4 2005, 10:13:39\n" +
				"#\n" +
				"#\n" +
				"\n" +
				"Stateflow {\n" +
				"\n" +
				"	machine {\n" +
				"		id                   		1\n" +
				"		name                 		\"damper\"\n" +
				"		created              		\"01-Jan-2006 12:00:00\"\n" +
				"		isLibrary            		0\n" +
				"		firstTarget          		2\n" +
				"		sfVersion            		62014000\n" +
				"	}\n" +
				"\n" +
				"	target {\n" +
				"		id                        		2\n" +
				"		name                      		\"sfun\"\n" +
				"		description               		\"Default Simulink S-Function Target.\"\n" +
				"		machine                   		1\n" +
				"		linkNode                  		[1 0 3]\n" +
				"	}\n" +
				"\n" +
				"	target {\n" +
				"		id                        		3\n" +
				"		name                      		\"rtw\"\n" +
				"		codeFlags                 		\" comments=1 statebitsets=1 databitsets=1 emitlogicalops=1 el\"\n" +
				"						\"seifdetection=1 constantfolding=1 redundantloadelimination=0\"\n" +
				"						\" preservenames=0 preservenameswithparent=0 exportcharts=0\"\n" +
				"		machine                   		1\n" +
				"		linkNode                  		[1 2 0]\n" +
				"	}\n" +
				"}\n";
		
		return body;
	}

	/** Generate the Block structure for an In Port */
	private String generateInPort(int id) {
		String s =  "		Block {\n" + 
		      		"		  BlockType		      Inport\n" + 
					"  		  Name		      \"In " + id + "\"\n" + 
					"     	  Position		      [20, "+ (50+id*30) + ", 40, "+ (60+id*30) + "]\n" +
					"         ShowName		      off\n" +
					"     	  IconDisplay	      \"Port number\"\n" +	
					"       }\n";
		return s;
	}
	
	/** Generate the Block structure for a Gain port */
	private String generateGain(int id, String gain) {
		String s =  "		Block {\n" + 
					"     	  BlockType		      Gain\n" + 
					" 	      Name		      \"Gain " + id + "\"\n" + 
					"     	  Position		      [100, "+ (50+id*30) + ", 120, "+ (60+id*30) + "]\n" +
					"         ShowName		      off\n" +
					"	      Gain		      \"1/" + gain + "\"\n" + 
					"    	  ParameterDataTypeMode   \"Inherit via internal rule\"\n" + 
					"	      OutDataTypeMode	      \"Inherit via internal rule\"\n" + 
					"    	  SaturateOnIntegerOverflow	off\n" + 
					"	    }\n";
		return s;
	}
	
	/** Generate the Block structure for a FP to Fix S-Function port */
	private String generateFPtoFix(int id) {
		String s =  "		Block {\n" + 
					"         BlockType		      Reference\n" + 
					"         Name		      \"FP to Fix " + id + "\"\n" + 
					"         Ports		      [1, 1]\n" + 
					"         Position		      [140, "+ (50+id*30) + ", 210, "+ (60+id*30) + "]\n" + 
					"         ShowName		      off\n" +
					"         BackgroundColor	      \"yellow\"\n" + 
					"         SourceBlock	      \"RTMDblocks/Convert FP to Fix\"\n" + 
					"         SourceType	      \"FPtoFix\"\n" + 
					"       }\n";
		return s;
	}
	
	/** Generate the Block structure for a SCRAMNet Write port */
	private String generateSCRWrite(int id, String partition, String name, String units) {
		String s =  "		Block {\n" + 
					"         BlockType		      Reference\n" + 
					"         Name		      " + name + " (" + units + ")_o" + "\n" + 
					"         Ports		      [1]\n" + 
					"         Position		      [230, "+ (50+id*30) + ", 260, "+ (60+id*30) + "]\n" + 
				    "         BackgroundColor	      \"green\"\n" +
					"         SourceBlock	      \"xpclib/Shared\\nMemory/Systran/SC150 write \"\n" + 
					"         SourceType	      \"SC150 Write\"\n" + 
					"         partition		      \"[" + partition + "]\"\n" + 
					"         ts		      \"sample\"\n" + 
					"         pci		      \"-1\"\n" + 
					"       }\n";
		return s;
	}
	
	/** Generate the Block structure for a SCRAMNet Read port */
	private String generateSCRRead(int id, String partition, String name, String units) {
		String s =  "	Block {\n" + 
					"     BlockType		      Reference\n" + 
					"     Name		      " + name + " (" + units + ")_i" + "\n" + 
					"     Ports		      [0, 1]\n" + 
					"     Position		      [20, "+ (50+id*30) +", 50, "+ (60+id*30) + "]\n" + 
					"     BackgroundColor	      \"orange\"\n" +
					"     SourceBlock	      \"xpclib/Shared\\nMemory/Systran/SC150 read \"\n" + 
					"     SourceType	      \"SC150 Read\"\n" + 
					"     partition		      \"[" + partition + "]\"\n" + 
					"     ts		      \"sample\"\n" + 
					"     pci		      \"-1\"\n" + 
					"   }\n";
		return s;
	}
	
	/** Generate the Block structure for a Fix to FP S-Function port */
	private String generateFixToFP(int id) {
		String s =  "	Block {\n" + 
					"     BlockType		      Reference\n" + 
					"     Name		      \"Fix to FP " + id + "\"\n" + 
					"     Ports		      [1, 1]\n" + 
					"     Position		      [70, "+ (50+id*30) + ", 130, "+ (60+id*30) + "]\n" + 
					"     ShowName		      off\n" +
					"     BackgroundColor	      \"yellow\"\n" + 
					"     SourceBlock	      \"RTMDblocks/Convert Fix to FP\"\n" + 
					"     SourceType	      \"FixtoFP\"\n" + 
					"   }\n";
		return s;
	}
	
	/** Generate the Block structure for a Limit Checker */
	private String generateLimitChecker(int id, String lowerlimit, String upperlimit) {
		String s =  "	Block {\n" +
					"		BlockType		      Reference\n" +
					"		Name		      \"Limit Checker " + id + "\"\n" +
					"		Ports		      [1, 1]\n" +
					"		Position		      [60, "+ (50+id*30) + ", 80, "+ (60+id*30) + "]\n" +
					"       BackgroundColor	      \"red\"\n" +
					"		SourceBlock	      \"RTMDblocks/Limit Checker\"\n" +
					"		SourceType	      \"\"\n" +
					"		ShowPortLabels	      \"FromPortIcon\"\n" +
					"		SystemSampleTime	      \"-1\"\n" +
					"		FunctionWithSeparateData off\n" +
					"		RTWMemSecFuncInitTerm   \"Inherit from model\"\n" +
					"		RTWMemSecFuncExecute    \"Inherit from model\"\n" +
					"		RTWMemSecDataConstants  \"Inherit from model\"\n" +
					"		RTWMemSecDataInternal   \"Inherit from model\"\n" +
					"		RTWMemSecDataParameters \"Inherit from model\"\n" +					
					"		upperlimit	      \"" + upperlimit + "\"\n" +
					"		lowerlimit	      \"" + lowerlimit + "\"\n" +
    				"		}\n";
		return s;
	}
	
	/** Generate the Block structure for a convert block to convert from uint32 to double */
	private String generateConverter(int id) {
		String s =  "		Block {\n" + 
					"     	  BlockType		      DataTypeConversion\n" + 
					" 	      Name		          \"Converter " + id + "\"\n" + 
					"     	  Position		      [70, "+ (50+id*30) + ", 90, "+ (60+id*30) + "]\n" +
					"         ShowName		      off\n" +
					"         OutDataTypeMode	  \"double\"\n" + 
			    	"         RndMeth	          \"Floor\"\n" +
			    	"         SaturateOnIntegerOverflow	      \"off\"\n" +
					"	    }\n";
		return s;
	}
	
	/** Generate the Block structure for Bus */
	private String generateBus(int id, String size, int offset) {
		String s =  "	Block {\n" + 
					"     BlockType		      BusCreator\n" + 
					"     Name		      \"Bus " + id + "\"\n" + 
					"     Ports		      ["+ size +", 1]\n" + 
					"     Position		      [160, "+ (50+offset*30) + ", 165, "+ (60+offset*30) + "]\n" + 
					"     ShowName		      off\n" + 
					"     Inputs		      \""+ size +"\"\n" + 
					"     DisplayOption	      \"bar\"\n" + 
					"   }\n";
		return s;
	}
	
	/** Generate the Block structure for a File Target port */
	private String generateFileTarget(int fid, int offset) {
		String s =  "	Block {\n" + 
					"     BlockType		      Reference\n" + 
					"     Name		      \"XPC" + fid + "\"\n" + 
					"     Ports		      [1]\n" + 
					"     Position		      [190, "+ (50+offset*30) + ", 250, "+ (60+offset*30) + "]\n" + 					
					"     SourceBlock	      \"xpclib/Misc./Scope (xPC) \"\n" + 
					"     SourceType	      \"xpcscopeblock\"\n" + 
					"     ShowPortLabels	      on\n" + 
					"     scopeno		      \""+fid+"\"\n" + 
					"     scopetype		      \"File\"\n" + 
					"     autostart		      on\n" + 
					"     viewmode		      \"Graphical redraw\"\n" + 
					"     formatstr		      \"'%15.6f'\"\n" + 
					"     grid		      on\n" + 
					"     ylimits		      \"[0,0]\"\n" + 
					"     nosamples		      \"1024\"\n" + 
					"     noprepostsamples	      \"0\"\n" + 
					"     interleave	      \"1\"\n" + 
					"     triggermode	      \"FreeRun\"\n" + 
					"     triggersignal	      \"1\"\n" + 
					"     triggerlevel	      \"0.0\"\n" + 
					"     triggerslope	      \"Either\"\n" + 
					"     triggerscope	      \"1\"\n" + 
					"     triggersample	      \"0\"\n" + 
					"     filename		      \"XPC"+ fid +".dat\"\n" + 
					"     mode		          \"Lazy\"\n" + 
					"     writesize		      \"512\"\n" + 
					"     autorestart	      on\n" + 
					"   }\n";
		return s;
	}	
	
	/** Generate the Block structure for a Line */
	private String generateLine(String source, String sport, String dest, String dport) {
		String s =  "	Line {\n" + 
					"     SrcBlock		      \""+ source +"\"\n" + 
					"     SrcPort		      "+ sport +"\n" + 
					"     DstBlock		      \""+ dest +"\"\n" + 
					"     DstPort		      "+ dport +"\n" +
					"    }\n";
		return s;
	}
	
	/** Replace white spaces with underscores */
	private String replaceWhiteWithUnderscore(String s) {
		return s.replaceAll(" ","_");
	}	
}

/** Template for the Matlab M File to Initialize the Model File */
class MTemplate {
	private String mdlname;
	private String body;
	
	/** Create a new MTemplate */
	public MTemplate(String modelname) {
		// store name of model
		mdlname = modelname;	
		// create body of file String
		body = new String();
	}
	
	/** Generate the text */
	public String generateM(XMLxPCConfig xml) {
		body = body + "%% xPC Initialization File for " + mdlname + ".mdl\n\n" + 				  
					  "%% Change this variable to set time\n" +
			          "stime = 1;\n\n" +
			          "sample = 1/1024;\n\n";
		
		// Generate the SCRAMNet Write Limits
		int id = 0;
		for (id = 0; id < xml.getnumxPCWriteBlocks(); id++) {
			if (xml.getxPCWriteisCTRL(id).equals("true")) 
				body = body + replaceWhiteWithUnderscore(xml.getxPCWriteName(id)) + "_lowerlimit = " + xml.getxPCWriteLowerLimit(id) + ";\n" + 
							  replaceWhiteWithUnderscore(xml.getxPCWriteName(id)) + "_upperlimit = " + xml.getxPCWriteUpperLimit(id) + ";\n";
		}
		
		body = body + "\n\n%% SCRAMNet Partitions\n" + 
					  "node.Interface.NodeID = '4';\n" +
					  "node = completenodestruct([],'scramnet');\n" +
					  "ptrigger.Address='0xFC';\n" +
					  "ptrigger.ExtTrigger2='all';\n" +
					  "completepartitionstruct(ptrigger,'scramnet');\n" + 
				      "pcameratrigger.Address='0xF4';\n" +		              
		              "completepartitionstruct(pcameratrigger,'scramnet');\n";
		
		
	    // Generate the Write block code
		id = 0;
		for (id = 0; id < xml.getnumxPCWriteBlocks(); id++) 
			body = body + generatePartition(id, xml.getxPCWriteLocation(id));
		
		// Generate the Read block code
		int idoffset = id;
		for (id = 0; id < xml.getnumxPCReadBlocks(); id++) 
			body = body + generatePartition((id+idoffset), xml.getxPCReadLocation(id));
		
		return body;
	}
	
	/** Generate the SCRAMNet partition code */
	private String generatePartition(int id, String address) {
		String body = "";
		body = 	"p" + id + ".Address='0x" + Integer.toHexString(Integer.parseInt(address)*4) + "';\n" + 
			  	"completepartitionstruct(p" + id + ",'scramnet');\n";		
		return body;
	}
	
	/** Replace white spaces with underscores */
	private String replaceWhiteWithUnderscore(String s) {
		return s.replaceAll(" ","_");
	}	
}
	
