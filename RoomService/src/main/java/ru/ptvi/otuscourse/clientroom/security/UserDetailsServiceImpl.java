package ru.ptvi.otuscourse.clientroom.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ptvi.otuscourse.clientroom.config.RoomConfigProps;
import ru.ptvi.otuscourse.clientroom.security.ClientRoomUserDetails;
import ru.ptvi.otuscourse.clientroom.service.RoomService;
import ru.ptvi.otuscourse.clientroomdto.ContragentDto;
import ru.ptvi.otuscourse.clientroomdto.ContragentWithDetailsDto;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RoomService roomService;

    private final String masterUserName;

    private final String masterPassword;

    public UserDetailsServiceImpl(RoomConfigProps props,
                                  RoomService roomService) {
        //log.info("Credentials for master: " + masterUserName + ", " + masterPassword);
        this.roomService = roomService;
        this.masterUserName = props.getSecurity().getMaster().getUsername();
        this.masterPassword = new BCryptPasswordEncoder(10).encode(props.getSecurity().getMaster().getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Try authentificate user: " + username);

        if (masterUserName.equals(username))
            return new ClientRoomUserDetails(username, masterPassword);

        Optional<ContragentWithDetailsDto> contragent = roomService.getContragentByEmailOrPhone(username);

        if (contragent.isEmpty())
            throw new UsernameNotFoundException(username + " not found");
        else
            return new ClientRoomUserDetails(username, contragent.get().password(), contragent.get().id());
    }
}
