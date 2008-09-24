useNikon = 1;

if (useNikon) 
    %% Copy latest image in the directory to the new directory
    try 
        nikon_images = dir ('nikon_capture');
        num_nikon_images = size(nikon_images);
        num_nikon_images = num_nikon_images(1);
        nikonimage = nikon_images(num_nikon_images).name;
        newimagenumber = zeroPad(stepcount, 6);
        newnikonimage = ['image' newimagenumber '.jpg'];
        %disp(sprintf('%s -> %s',[pwd '\nikon_capture\' nikonimage],[pwd '\nikon_experiment\' newnikonimage]));
        [status,message,messageid] = copyfile([pwd '\nikon_capture\' nikonimage],[pwd '\nikon_experiment\' newnikonimage]);
        %disp(message);
    catch 
        errmsg = lasterr;
        disp(errmsg);
    end
end
