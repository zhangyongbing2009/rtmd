% get the ground acceleration record and prepare as an effective force record
function Integrator = GetEQHistory(EQ_FILE, Integrator, Structure, Elements)

% load file
EQ = load(EQ_FILE);
InfluenceVector = zeros(Structure.NumFreeDOF, 1);
for i = 1:Structure.NumElements
    for j = 1:2
		if (Elements{i}.Nodes(j).UX <= Structure.NumFreeDOF)
			InfluenceVector(Elements{i}.Nodes(j).UX, 1) = 1.0;
		end
    end
end
%NegativeEFF = -1.0 * Integrator.EQScaleFactor * Structure.MassMatrixFree * InfluenceVector;
NegativeEFF = -1.0 * Structure.MassMatrixFree * InfluenceVector;

% save number of steps and effective force array to Integrator
Integrator.Steps = length(EQ);
Integrator.PEFF = EQ * NegativeEFF';