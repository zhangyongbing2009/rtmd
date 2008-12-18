% get the ground acceleration record and prepares as an effective force record
function Integrator = GetEQHistory(EQ_FILE, Integrator, Structure)

% load file
EQ = load(EQ_FILE);
InfluenceVector = zeros(Structure.NumFreeDOF, 1);
for i = 0:Structure.NumFreeDOF
    Y = 3.0 * i + 1;
    if (Y > Structure.NumFreeDOF)
        break;
    end
    InfluenceVector(Y, 1) = 1.0;
end
NegativeEFF = -1.0 * Integrator.EQScaleFactor * Structure.MassMatrixFree * InfluenceVector;
EQshift = zeros(1, 1);
EQ = cat(1, EQshift, EQ);
Integrator.Steps = length(EQ);
Integrator.PEFF = EQ * NegativeEFF';